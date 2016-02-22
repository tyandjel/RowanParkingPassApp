using System;
//using System.Collections.Generic;
//using System.Linq;
using System.Text;
using System.Threading;
using System.Net.Sockets;
using System.Net;
using System.Threading.Tasks;
using System.Diagnostics;
using System.Text.RegularExpressions;
using System.IO;
using System.Net.Security;
using System.Security.Authentication;
using System.Security.Cryptography.X509Certificates;
using System.Collections.Generic;

namespace ModServer
{
    /// <summary>
    /// Override this class and implement the abstract methods to set up a TCP server with custom protocol matchers and handlers.
    /// </summary>
    public abstract class TCPserver
    {
        public int TCP_CHUNK_SIZE = 4096;

        //public static String STR_HTTP = "(GET|POST) .* HTTP/.*\\..*";
        public static String STR_HTTP = "^(GET|POST).*HTTP";
        public static Regex HTTP = new Regex(STR_HTTP, RegexOptions.Compiled);

        private readonly TcpListener Listener;
        private bool listen = false;
        private Thread listenThread;

        private static Dictionary<TcpClient, Thread> clientTable = new Dictionary<TcpClient, Thread>();


        public static IPAddress getLocalIP()
        {
            IPHostEntry myhost = Dns.GetHostEntry(Dns.GetHostName());
            foreach (IPAddress ip in myhost.AddressList)
            {
                if (ip.AddressFamily == AddressFamily.InterNetwork)
                {
                    return ip;
                }
            }
            return IPAddress.Any;
        }

        public TCPserver(int port)
        {
            //look for port in use IPAddress.Parse("192.168.1.69")
            Listener = new TcpListener(IPAddress.Any, port);

        }

        public void kill()
        {
            listenThread.Abort(); // was interupt before
            Listener.Stop();
            DisconnectAll();
        }
        public void start()
        {
            if (listen == false)
            {
                listenThread = new Thread(new ThreadStart(ListenForClients));
                listen = true;
                listenThread.Start();
            }
            else
            {
                throw new NetworkException("The server cannot be started because it is already listening.");
            }
        }

        private IPAddress LocalIPAddress()
        {
            IPAddress localIP = null;
            try
            {
                IPHostEntry host;
                host = Dns.GetHostEntry(Dns.GetHostName());
                foreach (IPAddress ip in host.AddressList)
                {
                    if (ip.AddressFamily.ToString() == "InterNetwork")
                    {
                        localIP = ip;
                    }
                }
                if (localIP == null) { localIP = IPAddress.Parse("0.0.0.0"); }
                return localIP;
            }
            catch (Exception)
            {
                return IPAddress.Any;
            }
        }

        protected virtual void ListenForClients()
        {
            this.Listener.Start();

            while (listen)
            {
                if (!Listener.Pending())
                {
                    try
                    {
                        Thread.Sleep(500); // choose a number (in milliseconds) that makes sense
                    }
                    catch (ThreadInterruptedException) { listen = false; throw new SocketException(); };

                    continue; // skip to next iteration of loop
                }
                //blocks until a client has connected to the server

                TcpClient client = this.Listener.AcceptTcpClient();



                //create a thread to handle communication 
                //with connected client
                ThreadStart starter = delegate { HandleClientComm(client, generateProtocolEngines(client)); };
                Thread clientThread = new Thread(starter);

                clientTable.Add(client, clientThread); // disconnect may happen in HandleClientCom. Must come first so the entry is removed from client table
                clientThread.Start();
                //internet client table. Just for killing all threads

            }
        }

        protected abstract IProtocolMatcher[] generateProtocolEngines(TcpClient client);

        protected virtual void HandleClientComm(TcpClient client, IProtocolMatcher[] protocols)
        {
            if (protocols == null)
            {
                return;
            }
            Stream genericStream;
            NetworkStream clientStream = client.GetStream();
            //SslStream ssl = new SslStream(clientStream, false);
            //ssl.AuthenticateAsServer(new X509Certificate(MobiController.Properties.Resources.server, "instant"), false, SslProtocols.Tls12, true);

            genericStream = clientStream;
            BinaryWriter streamWriter = new BinaryWriter(genericStream, Encoding.UTF8);

            byte[] message = new byte[client.ReceiveBufferSize];
            String[] strmessage = { "" };
            int bytesRead;

            while (client.Client.Connected) // client.Client.Connected for thread interrupt
            {
                bytesRead = 0;

                try
                {
                    //blocks until a client sends a message
                    bytesRead = genericStream.Read(message, 0, TCP_CHUNK_SIZE);
                    if (!client.Client.Connected)
                    {
                        break;
                    }
                }
                catch
                {
                    //a socket error has occured
                    Debug.WriteLine("Error Occured");
                    //tcpClient.Close();
                    //Thread.CurrentThread.Abort();
                }

                if (bytesRead == 0)
                {
                    //the client has disconnected from the server
                    Debug.WriteLine("Client has disconnected");
                    break;
                }

                //message has successfully been received
                //String completeMessage = Encoding.UTF8.GetString(message, 0, bytesRead);
                bool protocolFound = false;

                //Array.Resize(ref message,bytesRead);
                byte[] cutMessage = new byte[bytesRead];
                for (int i = 0; i < bytesRead; i++)
                {
                    cutMessage[i] = message[i];
                }

                foreach (IProtocolMatcher protocol in protocols)
                {
                    if (protocol.IsMatch(cutMessage))
                    {
                        //sanity check. This point should never be concurrent!!
                        protocol.process();
                        protocolFound = true;
                        if (protocol.IsBlockingProtocol)
                        {
                            break; // don't check any other protocol.
                        }
                    }
                }

                //add support for custom protocol recognition (possibly give option of redirecting socket control)
                if (!protocolFound)
                {
                    try
                    {
                        streamWriter.Write(""); //to prevent weirdness
                    }
                    catch (Exception) { }; //for when socket closes
                    proccessUnknownProtocol();
                }

            }
        }

        //protected virtual void proccessHttp(HttpEngine http, String message)
        //{
        //    http.process(message.Split("\n".ToCharArray()));
        //}

        /// <summary>
        /// Add some functionality and arguments
        /// </summary>
        protected virtual void proccessUnknownProtocol()
        {
            Debug.WriteLine("No Matching Protocol Found.");
        }

        public virtual void Disconnect(TcpClient client)
        {
            if (client.Connected)
            {
                client.Close();
            }
            if (clientTable.ContainsKey(client))
            {
                Thread killThread = clientTable[client];
                clientTable.Remove(client); // !! watch out concurrency errors
                killThread.Abort();
                //killThread.Interrupt();
                //try
                //{
                //    client.GetStream().Dispose();
                //}
                //catch (ObjectDisposedException) { }
                //killThread.Abort(); is messy
            }
        }

        protected virtual void DisconnectAll()
        {
            TcpClient[] clients = new TcpClient[clientTable.Keys.Count];
            //To prevent concurrency errors
            clientTable.Keys.CopyTo(clients, 0);

            for (int i = 0; i < clients.Length; i++)
            {
                Disconnect(clients[i]);
            }
        }

        public virtual void DisconnectAll(string ip)
        {
            TcpClient[] clients = new TcpClient[clientTable.Keys.Count];
            //To prevent concurrency errors
            clientTable.Keys.CopyTo(clients, 0);

            for (int i = 0; i < clients.Length; i++)
            {
                string thisendpoint = clients[i].Client.RemoteEndPoint.ToString();
                string thisip = thisendpoint.Substring(0, thisendpoint.IndexOf(':'));
                if (thisip.Equals(ip))
                {
                    Disconnect(clients[i]);
                }
            }
        }

        //protected abstract HttpEngine getHttp;
    }
}
