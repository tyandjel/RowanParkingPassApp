using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;
using System.Net.Sockets;
using System.Net;
using System.Diagnostics;

namespace ModServer
{

    public abstract class HttpEngine : HelperClass
    {
        public static String PAGE_SIGN_IN = @"\auth.htm";
        public static String PAGE_NOT_FOUND = @"\404.htm";
        public static String PAGE_ROOT = "";

        public const String META_GET = "GET";
        public const String META_POST = "POST";
        public const String META_HEAD = "HEAD";

        public Stream Stream
        {
            get
            {
                return stream;
            }
            set
            {
                stream = value;
            }
        }

        public static Dictionary<string, string> pageMutations = new Dictionary<string, string>();

        public HttpEngine(Stream networkStream, TcpClient client)
            : base(networkStream, client)
        {
        }


        //private enum ;
        public virtual void process(String[] strmessage, byte[] message)
        {
            //Parses request into container object
            HttpRequest request = new HttpRequest(strmessage);
            switch (request.Method.ToUpper())
            {
                case META_GET:
                    processGet(request);
                    break;
                case META_POST:
                    processPost(request);
                    break;
                case META_HEAD:
                    processHead(request);
                    break;
                default:
                    break;
            }
        }

        public static bool addPageMutation(string placeholder, string replacement)
        {
            if (pageMutations.ContainsKey(placeholder))
            {
                return false;
            }
            pageMutations.Add(placeholder, replacement);
            return true;
        }

        /// <summary>
        /// This method checks that the request path exists in the PAGE_ROOT directory.
        /// Then the content-type is parsed from the request.
        /// An HttpRequest is generated and sent, followed by the file specified.
        /// Call this method conditionally from any subclass unless there are no conditions as to what clients are permitted to GET.
        /// </summary>
        /// <param name="request"></param>
        protected virtual bool processGet(HttpRequest request)
        {
            String relativePath = request.Path;
            if (relativePath.IndexOf('/') == 0)
            {
                relativePath = relativePath.Substring(1);
            }
            HttpResponse response;

            //Keep in mind that File.OpenRead defaultly uses FileShare.Read
            #region "File Checks and Response Generation"
            if (File.Exists(PAGE_ROOT + relativePath))
            {
                String Content = request.requestMetaInfo("Accept");
                response = new HttpResponse(HttpResponse.ConnectionStatus.OK, "keep-alive", PAGE_ROOT + relativePath);
                response.addHeader("Content-Type", request.getGuessFileType());
            }
            else
            {
                if (request.Path.Equals("/"))
                {
                    response = new HttpResponse(HttpResponse.ConnectionStatus.OK, "keep-alive", PAGE_ROOT + @"\" + onInit(request));
                    response.addHeader("Content-Type", "text/html");
                }
                else
                {
                    on404(request);
                    return false;
                }
            }
            #endregion


            using (FileStream s = File.OpenRead(response.FilePath))
            {
                response.addHeader("Content-Length", s.Length.ToString());
                try
                {
                    SocketWriteLine(response.ToString());
                    s.CopyTo(stream);
                }
                catch (SocketException)
                {
                    return false;
                }
                catch (IOException)
                {
                    return false;
                }
            }
            //}
            //catch (IOException e)
            //{
            //    Debug.WriteLine("send failure:  " + request.path);
            //}

            stream.Flush();
            return true;
        }

        protected virtual bool processHead(HttpRequest request) { return false; }

        protected void writeHtmlStream(Stream s, HttpResponse response)
        {
            String html;
            try
            {
                byte[] codedHtml = new byte[s.Length];
                s.Read(codedHtml, 0, (int)s.Length);
                html = Encoding.UTF8.GetString(codedHtml);
                foreach (KeyValuePair<string, string> kp in pageMutations)
                {
                    html = html.Replace(kp.Key, kp.Value);
                }
                response.addHeader("Content-Length", html.Length.ToString());
                SocketWriteLine(response.ToString());
                SocketWriteText(html);
            }
            catch (IOException) { }
            s.Close();

            stream.Flush();
        }

        protected void writeStream(Stream s, HttpResponse response)
        {
            try
            {
                response.addHeader("Content-Length", s.Length.ToString());
                SocketWriteLine(response.ToString());
                s.CopyTo(stream);
            }
            catch (IOException) { }
            s.Close();

            stream.Flush();
        }


        protected abstract void on404(HttpRequest request);

        protected abstract String onInit(HttpRequest request);

        protected virtual void processPost(HttpRequest request)
        {

        }
    }
}
