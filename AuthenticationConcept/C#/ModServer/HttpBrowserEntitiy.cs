using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net.Security;
using System.Net.Sockets;
using System.Security.Authentication;
using System.Security.Cryptography.X509Certificates;
using System.IO;

namespace ModServer
{
    public class HttpBrowserEntitiy
    {

        public enum PORTS : int { HTTP = 80, SSL = 443 };

        private Dictionary<String, String> cookies;
        public Dictionary<String, String> SessionCookies
        {
            get
            {
                return cookies;
            }
            set
            {
                cookies = value;
            }
        }

        /// <summary>
        /// http://msdn.microsoft.com/en-us/library/system.net.security.sslstream%28v=vs.110%29.aspx
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="certificate"></param>
        /// <param name="chain"></param>
        /// <param name="sslPolicyErrors"></param>
        /// <returns></returns>
        private static bool ValidateServerCertificate(
      object sender,
      X509Certificate certificate,
      X509Chain chain,
      SslPolicyErrors sslPolicyErrors)
        {
            if (sslPolicyErrors == SslPolicyErrors.None)
                return true;

            // Do not allow this client to communicate with unauthenticated servers. 
            return false;
        }

        public HttpBrowserEntitiy()
        {
            cookies = new Dictionary<string, string>();
        }

        // in the fure HttpConnections can have some form of callback or events that will register with the entity
        public HttpConnection openSecureConnection(string address, int port, string servername)
        {
            HttpConnection thisConnection;
            try
            {
                TcpClient client = new TcpClient(address, port);
                SslStream sslStream = new SslStream(
                    client.GetStream(),
                    false,
                    new RemoteCertificateValidationCallback(ValidateServerCertificate),
                    null
                    );
                // The server name must match the name on the server certificate. 
                sslStream.AuthenticateAsClient(servername); // may throw AuthenticationException
                thisConnection = new HttpConnection(address, port, servername, sslStream);
            }
            catch (SocketException se)
            {
                throw new NetworkException("SSL: Stream cannot be opened. Socket error " + se.ToString());
            }
            catch (IOException se)
            {
                throw new NetworkException("SSL: Stream cannot be opened. Socket error " + se.ToString());
            }

            return thisConnection;
        }

        public HttpConnection openSecureConnection(string address, PORTS port, string servername)
        { return openSecureConnection(address, (int)port, servername); }


        public HttpConnection openConnection(string address, int port)
        {
            HttpConnection thisConnection;
            try
            {
                TcpClient client = new TcpClient();

                client.Connect(address, port);

                thisConnection = new HttpConnection(address, port, address, client.GetStream());
            }
            catch (SocketException se)
            {
                throw new NetworkException("Stream cannot be opened. Socket error " + se.ToString());
            }

            return thisConnection;
        }
        public HttpConnection openConnection(string address, PORTS port)
        {
            return openConnection(address, (int)port);
        }

        public void processResponse(HttpResponse r)
        {
            foreach (KeyValuePair<string, string> kvp in r.cookies)
            {
                if (cookies.ContainsKey(kvp.Key))
                {
                    cookies.Remove(kvp.Key);
                }
                cookies.Add(kvp.Key, kvp.Value);
            }
        }

        public HttpConnection openURL(string url, int port)
        {
            string[] peices = splitURL(url);
            if (url.ToLower().StartsWith("https"))
            {
                return openSecureConnection(peices[0], (int)PORTS.SSL, peices[0]);
            }
            else
            {
                return openConnection(peices[0], port);
            }
        }

        /// <summary>
        /// Index 0 will be (http://)(www).domain.controller
        /// index 1 will be /path
        /// </summary>
        /// <param name="URL"></param>
        /// <returns></returns>
        public static string[] splitURL(string URL)
        {
            try
            {
                int position_of_dot = URL.IndexOf('.');
                int position_of_split = URL.IndexOf('/', position_of_dot);
                if (position_of_split == -1) { position_of_split = URL.Length; }
                string tail = URL.Substring(position_of_split, URL.Length - position_of_split);
                string front = URL.Substring(0, position_of_split);
                int position_of_lastSlash = front.LastIndexOf('/') + 1;
                if (tail.Equals("")) { tail = "/"; }
                return new string[2] { front.Substring(position_of_lastSlash, front.Length - position_of_lastSlash), tail };
            }
            catch (ArgumentException)
            {
                throw new NetworkException("Invalid URL");
            }
        }

    }
}
