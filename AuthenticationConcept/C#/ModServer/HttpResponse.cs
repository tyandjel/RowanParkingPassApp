using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Collections;
using System.Text.RegularExpressions;

//NOTICE: added toLower in header keys

namespace ModServer
{

    public class HttpResponse
    {
        public enum ConnectionStatus { Continue = 100, OK = 200, NO_CONTENT = 205, RESET_CONTENT = 205, CREATED = 201, ACCEPTED = 202, FOUND = 302, TEMP_REDIRECT = 307, FORBIDDEN = 403, FILE_NOT_FOUND = 404, IM_A_TEAPOT = 418, NOT_IMPLEMENTED = 501 };
        private string filePath;
        public string FilePath
        {
            get
            {
                return filePath;
            }
            set
            {
                filePath = value;
            }
        }
        public readonly string version = "HTTP/1.1";
        private ConnectionStatus status;
        public ConnectionStatus Status
        {
            get
            {
                return status;
            }
            set
            {
                status = value;
            }
        }
        private string body;
        public string Body
        {
            get
            {
                return body;
            }
            set
            {
                body = value;
            }
        }

        private byte[] messageRAW;
        public byte[] MessageRAW
        {
            get
            {
                return messageRAW;
            }
            set
            {
                if (messageRAW == null)
                {
                    messageRAW = value;
                }
                else
                {
                    var tmpList = messageRAW.ToList();
                    tmpList.AddRange(value);
                    messageRAW = tmpList.ToArray();
                }
            }
        }

        private int headLength;

        public int HeadLength
        {
            get { return headLength; }
            set { headLength = value; }
        }

        public Dictionary<String, String> headers;
        public Dictionary<String, String> cookies;

        public HttpResponse(ConnectionStatus status, String Connection, String filePath)
        {
            cookies = new Dictionary<String, String>();
            headers = new Dictionary<String, String>();
            this.status = status;
            this.filePath = filePath;

            //headers.Add("Allow", "GET");
            headers.Add("Date", DateTime.Now.ToString("r"));
            headers.Add("Server", "MyMobiControllerServer");
            headers.Add("Connection", Connection);
        }

        public void clearMessageBytes()
        {
            this.messageRAW=null;
        }

        public static bool validateResponse(String[] request)
        {
            String[] header = request[0].Split(" ".ToCharArray());
            if (header.Length != 3)
            {
                return false;
            }
            try
            {
                var status = (ConnectionStatus)Convert.ToInt32(header[1]);
            }
            catch (FormatException)
            {
                return false;
            }
            return true;
        }

        public HttpResponse(String[] request)
        {
            bool headersOver = false;
            StringBuilder msgBody = new StringBuilder();

            headers = new Dictionary<String, String>();
            cookies = new Dictionary<String, String>();

            String[] header = request[0].Split(" ".ToCharArray());
            if (header.Length > 2)
            {
                version = header[0].Trim();
                try
                {
                    status = (ConnectionStatus)Convert.ToInt32(header[1]);
                }
                catch (FormatException)
                {
                }
            }
            headLength = request[0].Length + Environment.NewLine.Length;

            //-1 for the blank line at the end
            for (int i = 1; i < request.Length; i++)
            {
                if (!headersOver && request[i].Length > 1)
                {
                    headLength += request[i].Length + Environment.NewLine.Length;
                    int delimeterposition = request[i].IndexOf(":");
                    string[] tokens = new String[2];
                    tokens[0] = request[i].Substring(0, delimeterposition).ToLower();
                    tokens[1] = request[i].Substring(delimeterposition + 1);
                    switch (tokens[0])
                    {
                        case "set-cookie":
                            //executes the parseCookie method for every name value pair of cookies
                            try
                            {
                                foreach (Match match in HttpRequest.COOKIE_PROCESSING_REGEX.Matches(tokens[1]))
                                {
                                    try
                                    {
                                        cookies.Add(match.Groups[1].Value.Replace("\r", ""), match.Groups[2].Value.Replace("\r", ""));
                                    }
                                    catch (ArgumentException)
                                    {
                                        cookies.Remove(match.Groups[1].Value.Replace("\r", ""));
                                        cookies.Add(match.Groups[1].Value.Replace("\r", ""), match.Groups[2].Value.Replace("\r", ""));
                                    }
                                }
                                //Array.ForEach(tokens[1].Split(";".ToCharArray()), new Action<string>(parseCookie));
                            }
                            catch (IndexOutOfRangeException)
                            {
                                throw new HttpHeaderParseException("Cookies incorrectly formatted.");
                            }
                            break;
                        default:
                            String headerKey = tokens[0].Trim().ToLower();
                            String headerVal = tokens[1].Trim();
                            if (headers.ContainsKey(headerKey))
                            {
                                headers[headerKey] = headerVal;
                            }
                            else
                            {
                                headers.Add(headerKey, headerVal); // Do not put the cookie header here. It will be added manualy
                            }
                            break;
                    }
                }
                else
                {
                    if (request[i].Equals("")) // flag so all the rest of the lines are not interpreted as headers
                    {
                        if (headersOver == false)
                        {
                            HeadLength += Environment.NewLine.Length;
                        }
                        headersOver = true;
                    }
                    else
                    {
                        msgBody.AppendLine(request[i]);
                    }
                }
            }
            body = msgBody.ToString();
        }

        public void addHeader(String field, String data)
        {
            if (headers.ContainsKey(field))
                headers.Remove(field);
            headers.Add(field, data);
        }

        public void setCookie(String name, String value)
        {
            if (cookies.ContainsKey(name))
                cookies.Remove(value);
            cookies.Add(name, value);
        }

        public void guessContentLength()
        {
            addHeader("Content-Length", Body.Length.ToString());
        }

        public override String ToString()
        {
            StringBuilder returnString = new StringBuilder(version);
            returnString.Append(" ");
            returnString.Append(((int)status));
            returnString.Append(" ");
            returnString.AppendLine(status.ToString());

            foreach (KeyValuePair<String, String> header in headers)
            {
                returnString.Append(header.Key);
                returnString.Append(": ");
                returnString.AppendLine(header.Value);
            }
            var tmpString = new StringBuilder();
            foreach (KeyValuePair<String, String> kvp in cookies)
            {
                tmpString.Append(kvp.Key);
                tmpString.Append("=");
                if (kvp.Value.EndsWith(";"))
                {
                    tmpString.Append(kvp.Value);
                    //returnString.Append(" ");
                }
                else
                {
                    tmpString.Append(kvp.Value);
                    tmpString.Append(";");
                    //returnString.Append(" ");
                }
            }
            if (cookies.Count > 0)
            {
                tmpString.Insert(0,"Set-Cookie: ");
                tmpString.AppendLine();
                returnString.Append(tmpString);
            }
            if (Body != null && Body.Length > 0)
            {
                returnString.Append(Environment.NewLine);
                returnString.AppendLine(Body);
            }
            return returnString.ToString(); //+ "\r"; //signal end of message (http protocol)
        }
    }
}
