using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;
using System.Threading;
using System.Net.Sockets;

namespace ModServer
{
    [Serializable()]
    public class ConnectionException : Exception
    {
        public ConnectionException() : base() { }
        public ConnectionException(string message) : base(message) { }
        public ConnectionException(string message, Exception inner) : base(message, inner) { }
    }
    /// <summary>
    /// This class is uses Microsoft's implementation of SSL. Most of the code is copied 
    /// from: http://msdn.microsoft.com/en-us/library/system.net.security.sslstream%28v=vs.110%29.aspx
    /// </summary>
    public class HttpConnection
    {
        public static int BUFFER_SIZE = 4048;
        public static long DL_CACHE_SIZE = 1048576;
        private string address;
        public string Address
        {
            get
            {
                return address;
            }
            set
            {
                address = value;
            }
        }

        private int port = 80;

        public int Port
        {
            get { return port; }
            set { port = value; }
        }

        private string servername;

        private Dictionary<String, String> cookies;

        public Dictionary<String, String> Cookies
        {
            get { return cookies; }
            set { cookies = value; }
        }

        public static string CHUNKED_TERMINATOR = "0" + Environment.NewLine + Environment.NewLine;

        public Stream stream;
        public delegate void AwaitMessageCallback(HttpResponse r, Exception ex);

        public HttpConnection(string address, int port, string servername, Stream stream)
        {
            this.address = address;
            this.port = port;
            this.servername = servername;
            this.stream = stream;

            cookies = new Dictionary<string, string>();
        }

        public void close()
        {
            stream.Close();
        }

        public HttpResponse sendRequest(HttpRequest request)
        {
            request.addHeader("Host", servername);
            string thisstring = request.ToString();
            string sendme = request.ToString();
            byte[] outBytes = Encoding.UTF8.GetBytes(sendme);
            //stream.Flush();
            stream.Write(outBytes, 0, outBytes.Length);
            stream.Flush();

            return awaitMessage();
        }

        /// <summary>
        /// 
        /// MOVED the requesting to a new thread and added the callBack error
        /// 
        /// </summary>
        /// <param name="request"></param>
        /// <param name="callBack">response null if exception. exception null otehrwise</param>
        /// <returns></returns>
        public Thread sendRequestAsync(HttpRequest request, AwaitMessageCallback callBack)
        {
            Thread listenThread;
            listenThread = new Thread(() =>
            {
                try
                {
                    request.addHeader("Host", servername);

                    string thisstring = request.ToString();
                    string sendme = request.ToString();
                    byte[] outBytes = Encoding.UTF8.GetBytes(sendme);

                    //try catch here with a fail callback and timeout timer
                    stream.Write(outBytes, 0, outBytes.Length);
                    stream.Flush();
                }
                catch (Exception ex)
                {
                    callBack(null, ex);
                    return;
                }
                callBack(awaitMessage(), null); //Call back should not catch errors
            });
            listenThread.Start();
            return listenThread;
        }

        public byte[] takeByte(byte[] buffer)
        {
            int read = 0;
            try
            {
                read = stream.Read(buffer, 0, BUFFER_SIZE);
            }
            catch (System.ArgumentOutOfRangeException)
            {
                return null;
            }
            catch (System.IO.IOException)
            {
                return null;
            }
            if (read == 0)
            {
                return null;
            }
            byte[] tmp = new byte[read];
            for (int i = 0; i < read; i++)
            {
                tmp[i] = buffer[i];
            }
            return tmp;
        }

        public HttpResponse awaitMessage()
        {
            byte[] buffer = new byte[BUFFER_SIZE];
            string message = "";

            HttpResponse response = null;

            bool Cont = true;
            bool ResponseReceived = false;//flag for init
            string tmp = "";
            do
            {
                buffer = new byte[BUFFER_SIZE];
                if ((buffer = this.takeByte(buffer)) == null)
                {
                    Cont = false;
                }
                else
                {
                    message += Encoding.UTF8.GetString(buffer);
                }


                if (!ResponseReceived)
                {
                    var request_line = message.Split(new string[] { Environment.NewLine }, StringSplitOptions.None);
                    if (HttpResponse.validateResponse(request_line))
                    {
                        response = new HttpResponse(request_line);
                        response.clearMessageBytes();
                        ResponseReceived = true;
                        response.MessageRAW = Encoding.UTF8.GetBytes(message);
                    }
                    else
                    {
                        // wtf is this.. should at least be a header?!
                        tmp += message;
                        continue;
                        //ResponseReceived = false;
                    }
                }
                else {
                    // response created. headers should be done.
                    response.MessageRAW = buffer;
                    response.Body = message.Substring(response.HeadLength);
                }
                // Checking for termination - chunked encoding and content-length aware
                if (response.headers.ContainsKey("transfer-encoding") && response.headers["transfer-encoding"].Equals("chunked"))
                {
                    if (message.Contains(CHUNKED_TERMINATOR))
                    {
                        message = message.Remove(message.IndexOf(CHUNKED_TERMINATOR));
                        Cont = false;
                    }
                    ResponseReceived = true;
                }
                else
                {
                    //ResponseReceived = response.Status != 0;
                    if (ResponseReceived)
                    {
                        // The server may not be done spitting info at us
                        // checking content length
                        if (response.headers.ContainsKey("content-length"))
                        {
                            Cont = true; // must set based off content-length
                            int contentLen = Convert.ToInt32(response.headers["content-length"]);
                            if (response.MessageRAW.Length >= contentLen+response.HeadLength)
                            {
                                Cont = false;
                            }
                            else
                            {
                                buffer = new byte[BUFFER_SIZE]; // Careful of them overflowz
                                Cont = true;
                                //if ((buffer = this.takeByte(buffer)) == null)
                                //{
                                //    Cont = false;
                                //}
                                //else
                                //{
                                //    ResponseReceived = true;
                                //    message = Encoding.UTF8.GetString(buffer).Trim();
                                //    response.MessageRAW = buffer;
                                //    response.Body += message;
                                //    if (response.MessageRAW.Length >= contentLen + response.HeadLength)
                                //    {
                                //        Cont = false;
                                //    }
                                //}
                            }
                        }
                        else
                        {
                            if (BUFFER_SIZE == buffer.Length)
                            {
                                // in case terminator perfetly aligns at end
                                if (!message.EndsWith(Environment.NewLine))
                                {
                                    // if buffer full to brim and has EOL
                                    Cont = false;
                                }
                            }
                            else
                            {
                                // if no content-length and buffer not filled
                                Cont = false;
                            }
                        }
                    }
                }
            } while (Cont);

            if(response == null)
            {
                return null;
            }
            // may want to check if return code is > 200 for clearing referer
            return processResponse(response);
        }

        public static void nullAction(int param1)
        {
            return;
        }

        public bool continueDownload(Stream output, HttpResponse response, Action<int> setup, Action<int> step, Action whenfinished)
        {
            bool tmp = continueDownload(output, response, setup, step);
            whenfinished();
            return tmp;
        }

        public bool continueDownload(Stream output, HttpResponse response, Action<int> setup, Action<int> step)
        {
            if ((int)response.Status < 300)
            {
                try
                {
                    if (!response.headers.ContainsKey("content-length"))
                    {
                        throw new ConnectionException("Content Length needed");
                    }
                    int contentLen = Convert.ToInt32(response.headers["content-length"]);
                    int filepartlen = response.MessageRAW.Length - response.HeadLength;
                    int restLen = contentLen - filepartlen;

                    setup(contentLen);

                    output.Write(response.MessageRAW, response.HeadLength, filepartlen);
                    step(filepartlen);

                    byte[] rest = new byte[DL_CACHE_SIZE]; // 1mb of buffer space
                    int read = 0;
                    while (1 < restLen)
                    {
                        read = stream.Read(rest, 0, rest.Length);
                        output.Write(rest, 0, read);
                        restLen -= read;
                        step(read);
                        output.Flush();
                    }
                }
                catch (FormatException)
                {
                    return false;
                }
                catch (IOException)
                {
                    return false;
                }
                return true;
            }
            else
            {
                throw new ConnectionException("Download failed.");
            }
        }

        private HttpResponse processResponse(HttpResponse response)
        {
            foreach (KeyValuePair<String, String> kvp in response.cookies)
            {
                if (cookies.ContainsKey(kvp.Key))
                    cookies.Remove(kvp.Key);
                cookies.Add(kvp.Key, kvp.Value);
            }
            return response;
        }
    }
}
