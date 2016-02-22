using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net.Sockets;

using System.IO;
using System.Net;

namespace ModServer
{
    [Serializable()]
    public class HelperClass
    {

        protected Stream stream;
        [NonSerialized]
        protected TcpClient client;

        public HelperClass(Stream stream, TcpClient client)
        {
            this.stream = stream;
            this.client = client;
        }

        //public static Dictionary<string, string> parseDictionary(string parseMe, char seperator, char terminator, bool isTrimmed = false)
        //{
        //    var returnme = new Dictionary<string, string>();
        //    Array.ForEach(parseMe.Split(seperator), );
        //    return returnme;
        //}

        //public 

        //public static Dictionary<string, string> parseKVP(string parseMe, char seperator, char terminator, bool isTrimmed = false)
        //{
        //    var returnme = new Dictionary<string, string>();
        //    StringBuilder key = new StringBuilder();
        //    StringBuilder val = new StringBuilder();
        //    bool iskey = true;
        //    //for (int i = 0; i < parseMe.Length; i++)
        //    //{
        //    //    worker = new StringBuilder();
        //    //    while(!(parseMe[i].Equals(seperator))){
        //    //        worker.Append(parseMe[i]);
        //    //        i++;
        //    //    }
        //    //    worker = new StringBuilder();
        //    //    while (!(parseMe[i].Equals(terminator)))
        //    //    {

        //    //    }
        //    //}
        //    foreach (char thisChar in parseMe)
        //    {
        //        if (iskey)
        //        {
        //            if (thisChar.Equals(seperator))
        //            {
        //                iskey = false;
        //                continue;
        //            }
        //            key.Append(thisChar);
        //        }
        //        else
        //        {
        //            if (thisChar.Equals(terminator))
        //            {
        //                string Key = key.ToString();
        //                string Val = val.ToString();
        //                if (isTrimmed)
        //                {
        //                    Key.Trim();
        //                    Val.Trim();
        //                }
        //                returnme.Add(Key, Val);
        //                key = new StringBuilder();
        //                val = new StringBuilder();
        //                iskey = true;
        //                continue;
        //            }
        //            val.Append(thisChar);
        //        }
        //        if (!parseMe.EndsWith(terminator.ToString()))
        //        {
        //            string Key = key.ToString();
        //            string Val = val.ToString();
        //            if (isTrimmed)
        //            {
        //                Key.Trim();
        //                Val.Trim();
        //            }
        //            returnme.Add(Key, Val);
        //        }
        //    }
        //    return returnme;
        //}

        /// <summary>
        /// Attempts to convert a string of key value pairs to a Dictionary object.
        /// </summary>
        /// <param name="parseMe">The string to parse</param>
        /// <param name="seperator">The string between the key and the value</param>
        /// <param name="terminator">The string seperating each key value pair</param>
        /// <param name="trim"></param>
        /// <param name="transform">A function that takes one string parameter and returns a transformed version of the text. Applied to both key and value AFTER parsing</param>
        /// <returns></returns>
        public static Dictionary<string, string> parseKVP(string parseMe, char seperator, char terminator, bool trim = false, Func<string, string> transform = null)
        {
            var returnme = new Dictionary<string, string>();
            if (!parseMe.Contains(seperator))
            {
                return returnme;
            }
            foreach (string kvp in parseMe.Split(terminator))
            {
                int seperatorPosition = kvp.IndexOf(seperator); // DONT DO THIS ALLOW '=' in the val. use substring and find first instance
                if (seperatorPosition < 0)
                {
                    continue;
                }
                string Key = kvp.Substring(0,seperatorPosition);
                string Val = kvp.Substring(seperatorPosition+1);
                if (transform != null)
                {
                    Key = transform(Key);
                    Val = transform(Val);
                }
                if (trim)
                {
                    Key = Key.Trim();
                    Val = Val.Trim();
                }
                returnme.Add(Key, Val);
            }


            return returnme;
        }

        public static Func<string, string> transformNull = (input) => { return input; };
        public static Func<string, string> transformURLDecode = (input) => { return WebUtility.UrlDecode(input); };

        public static Dictionary<T, T2> mergeDictionaries<T, T2>(Dictionary<T, T2> primary, Dictionary<T, T2> secondary)
            where T : class
            where T2 : class
        {
            var returnme = new Dictionary<T, T2>(primary);

            foreach (KeyValuePair<T, T2> kvp in secondary)
            {
                try
                {
                    returnme.Add(kvp.Key, kvp.Value);
                }
                catch (ArgumentException) { }
            }
            return returnme;
        }

        /// <summary>
        /// Writes a string out to the stream with a newline in UTF8 format
        /// </summary>
        /// <param name="message"></param>
        public void SocketWriteLine(String message)
        {
            if (stream.CanWrite)
            {
                //BinaryForrmatter will aslo do this
                byte[] realMessage = System.Text.Encoding.UTF8.GetBytes(message + Environment.NewLine);
                stream.Write(realMessage, 0, realMessage.Length);
            }
            else
            {
                throw new Exception("Cannot write to stream.");
            }
            //stream.Flush();
        }

        /// <summary>
        /// Writing a string to the socket in UTF8 format
        /// </summary>
        /// <param name="message"></param>
        public void SocketWriteText(String message)
        {
            if (stream.CanWrite)
            {
                byte[] realMessage = System.Text.Encoding.UTF8.GetBytes(message);
                stream.Write(realMessage, 0, realMessage.Length);
            }
            else
            {
                throw new Exception("Cannot write to stream.");
            }
            //stream.Flush();
        }

        /// <summary>
        /// Returns a chunk of a string that is between two other strings.
        /// </summary>
        /// <param name="right">right outer bound</param>
        /// <param name="left">left outerbound</param>
        /// <param name="whole">Whole String</param>
        /// <returns>inner string between 'right' and 'left'</returns>
        public static string getIsolatedString(string right, string left, string whole)
        {
            int rightindex = whole.IndexOf(right);
            if (rightindex < 0)
            {
                return whole;
            }
            rightindex += right.Length;

            int leftindex;
            try
            {
                leftindex = whole.IndexOf(left, rightindex + 1);
            }
            catch (ArgumentOutOfRangeException)
            {
                return whole.Substring(rightindex);
            }
            if (leftindex == -1)
            {
                leftindex = whole.Length;
            }
            return whole.Substring(rightindex, leftindex - rightindex);
        }

        /// <summary>
        /// Attempts to turn html text into plain text by removing hyperlinks and page breaks
        /// </summary>
        /// <param name="input"></param>
        /// <returns></returns>
        public static string stripHtmlParagraph(string input)
        {
            int thisend;
            while ((thisend = input.IndexOf("<a")) != -1)
            {
                input = input.Substring(0, thisend) + input.Substring(input.IndexOf(">", input.IndexOf("</a", thisend)) + 1);
                thisend = 0;
            }
            return input.Replace("<br />", "").Replace("<br/>", "").Replace("<br>", "").Trim();
        }
    }
}
