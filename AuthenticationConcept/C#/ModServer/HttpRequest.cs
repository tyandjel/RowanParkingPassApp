using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Collections;
using System.Text.RegularExpressions;
using System.Net;

namespace ModServer
{
    public class HttpRequest
    {
        private static int newlineLength = Environment.NewLine.Length;
        public static Regex POST_PROCESSING_REGEX = new Regex(@"(?<name>[^&].[^&|=]+)=(?<value>.[^&]*)", RegexOptions.Compiled);
        public static Regex COOKIE_PROCESSING_REGEX = new Regex("(?<name>[^;| ].[^;|=]+)=(?<value>.[^;| ]+)", RegexOptions.Compiled);

        private String method;

        public String Method
        {
            get { return method; }
            set { method = value; }
        }
        private String path;

        public String Path
        {
            get { return path; }
            set { path = value; }
        }
        private String version = "HTTP/1.1";

        public String Version
        {
            get { return version; }
            set { version = value; }
        }
        private String messageBody;

        public String MessageBody
        {
            get { return messageBody; }
            set { messageBody = value; }
        }

        public Dictionary<String, String> headers;
        public Dictionary<String, String> cookies;

        public Dictionary<String, String> postValues;
        public readonly Dictionary<String, String> getValues;

        private int headerLength;

        public int HeaderLength
        {
            get { return headerLength; }
            set { headerLength = value; }
        }


        public HttpRequest(String method, String path, String UserAgent = "Mobicontroller/1.0 (mobicontroller.com)", String ConnectionType = "keep-alive", String Accept = "text/html", String ContentType = "text/html")
        {
            headers = new Dictionary<String, String>();
            postValues = new Dictionary<String, String>();
            getValues = new Dictionary<String, String>();
            cookies = new Dictionary<String, String>();

            this.method = method;
            this.path = path;
            this.messageBody = "";

            addHeader("User-Agent", UserAgent);
            addHeader("Connection", ConnectionType);
            addHeader("Accept", Accept);
            addHeader("Content-Type", ContentType);
        }

        public HttpRequest(String path) : this("GET", path) { }

        public HttpRequest(String[] request)
        {
            headers = new Dictionary<String, String>();
            postValues = new Dictionary<String, String>();
            getValues = new Dictionary<String, String>();
            cookies = new Dictionary<String, String>();
            headerLength = 0;


            bool headersOver = false;
            StringBuilder Body = new StringBuilder();

            int i = 0; // To prezerve place in loop, must have initial value
            //try
            //{
            String[] header = request[0].Split(" ".ToCharArray());
            method = header[0].Trim();
            path = header[1].Trim();
            version = header[2].Trim();
            if (path.Contains('?'))
            {
                String[] pathparts = path.Split('?');
                //foreach (Match match in POST_PROCESSING_REGEX.Matches(WebUtility.UrlDecode(pathparts[1])))
                //{
                //    getValues.Add(match.Groups[1].ToString(), match.Groups[2].Value);
                //}
                getValues = HelperClass.parseKVP(pathparts[1], '=', '&', false, HelperClass.transformURLDecode);
                path = pathparts[0];
            }

            headerLength += request[0].Length + newlineLength;

            //-1 for the blank line at the end
            for (i = 1; i < request.Length; i++)
            {
                if (!headersOver && request[i].Length > 1)
                {
                    headerLength += request[i].Length + newlineLength;

                    int delimeterposition = request[i].IndexOf(":");
                    string[] tokens = new String[2];
                    tokens[0] = request[i].Substring(0, delimeterposition).ToLower();
                    tokens[1] = request[i].Substring(delimeterposition + 1);
                    switch (tokens[0])
                    {
                        case "cookie":
                            //executes the parseCookie method for every name value pair of cookies
                            try
                            {
                                //foreach (Match match in COOKIE_PROCESSING_REGEX.Matches(tokens[1]))
                                //{
                                //    try
                                //    {
                                //        cookies.Add(match.Groups[1].Value, match.Groups[2].Value);
                                //    }
                                //    catch (ArgumentException)
                                //    {
                                //        cookies.Remove(match.Groups[1].Value);
                                //        cookies.Add(match.Groups[1].Value, match.Groups[2].Value);
                                //    }
                                //}
                                cookies = HelperClass.mergeDictionaries(cookies, HelperClass.parseKVP(tokens[1], '=', ';', true));
                                //Array.ForEach(tokens[1].Split(";".ToCharArray()), new Action<string>(parseCookie));
                            }
                            catch (IndexOutOfRangeException)
                            {
                                throw new HttpHeaderParseException("Cookies incorrectly formatted.");
                            }
                            break;
                        default:
                            headers.Add(tokens[0].Trim(), tokens[1].Trim()); // Do not put the cookie header here. It will be added manualy
                            break;
                    }
                }
                else
                {
                    Body.AppendLine(request[i]);
                    if (request[i].Equals("") || request[i].Equals("\r") || request[i].Equals("\n"))
                    {
                        if (!headersOver)
                        {
                            headerLength += request[i].Length + newlineLength;
                            Body = new StringBuilder();
                        }
                        headersOver = true;
                    }
                    postValues = HelperClass.mergeDictionaries(postValues, HelperClass.parseKVP(request[i], '=', '&', false, HelperClass.transformNull));
                    //foreach (Match match in POST_PROCESSING_REGEX.Matches(WebUtility.UrlDecode(request[i]))) //will kill the thread on bad data (\0)
                    //{
                    //    try
                    //    {
                    //        postValues.Add(match.Groups[1].Value.Trim(), match.Groups[2].Value.Trim());
                    //    }
                    //    catch (ArgumentException) { }
                    //}
                }
            }
            messageBody = Body.ToString();
        }

        [Obsolete("Replaced by Regex.")]
        private void parseCookie(String cookie)
        {
            String[] tmp = cookie.Split('=');
            cookies.Add(WebUtility.UrlDecode(tmp[0]).Trim(), WebUtility.UrlDecode(tmp[1]).Trim()); //regex should take care of trailing \r\n
        }

        public string getGuessFileType()
        {
            String Content;
            try
            {
                Content = requestMetaInfo("Accept");
            }
            catch (KeyNotFoundException)
            {
                Content = "";
            }
            if (Content.Length > 1)
            {
                string[] contentformat = Content.Split("/".ToCharArray());
                int commapos = contentformat[1].IndexOf(',');
                if (commapos > 0)
                {
                    contentformat[1] = contentformat[1].Substring(0, commapos);
                }
                Content = contentformat[0] + "/" + contentformat[1];
                return Content;
            }
            else
            {
                return "text/html";
            }
        }

        public String requestMetaInfo(String field)
        {
            field = field.ToLower();
            try
            {
                return headers[field];
            }
            catch (KeyNotFoundException)
            {
                return "";
            }
        }

        /// <summary>
        /// Will add a header to the collection of headers.
        /// !! Will overwrite headers with the same field
        /// </summary>
        /// <param name="field"></param>
        /// <param name="data"></param>
        public void addHeader(String field, String data)
        {
            if (headers.ContainsKey(field))
                headers.Remove(field);
            headers.Add(field, data);
        }

        public void addPost(String field, String data)
        {
            if (postValues.ContainsKey(field))
                postValues.Remove(field);
            postValues.Add(field, data);
        }

        public string getPostBodyData(bool calculateContentLength)
        {
            var tempString = new StringBuilder();
            foreach (KeyValuePair<String, String> kvp in postValues)
            {
                tempString.Append(WebUtility.UrlEncode(kvp.Key));
                tempString.Append("=");
                tempString.Append(WebUtility.UrlEncode(kvp.Value));
                tempString.Append("&");
            }
            if (postValues.Count != 0)
            {
                tempString.Remove(tempString.Length - 1, 1); //get rid of last '&'

            }
            if (calculateContentLength)
            {
                addHeader("Content-Length", tempString.Length.ToString());
            }
            return tempString.ToString();
        }

        public void guessContentLength()
        {
            getPostBodyData(true);
        }

        /// <summary> 
        /// </summary>
        /// <returns>a properly formatted Http Request Packet</returns>
        public override string ToString()
        {
            StringBuilder returnString = new StringBuilder(method);
            returnString.Append(" ");
            returnString.Append(path);
            returnString.Append(" ");
            returnString.Append(version);
            returnString.Append(Environment.NewLine);

            foreach (KeyValuePair<String, String> kvp in headers)
            {
                returnString.Append(kvp.Key);
                returnString.Append(": ");
                returnString.Append(kvp.Value);
                returnString.Append(Environment.NewLine);
            }
            StringBuilder tempString = new StringBuilder();

            // this needs to be here for the content-length header to be in the result
            string body = getPostBodyData(!headers.ContainsKey("Content-Length"));

            // add in the cookie header
            foreach (KeyValuePair<String, String> kvp in cookies)
            {
                tempString.Append(kvp.Key);
                tempString.Append("=");
                tempString.Append(kvp.Value);
                tempString.Append(";");
            }
            if (tempString.Length != 0)
            {
                tempString.Remove(tempString.Length - 1, 1); // get rid of last ';'
                tempString.Insert(0, "Cookie: "); // in case no cookies
            }
            returnString.Append(tempString);
            returnString.Append(Environment.NewLine+Environment.NewLine);
            returnString.AppendLine(body + Environment.NewLine);

            return returnString.ToString();
        }
    }

}
