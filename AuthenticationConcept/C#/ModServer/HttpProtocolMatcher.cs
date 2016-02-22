using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Text.RegularExpressions;

namespace ModServer
{
    public class HttpProtocolMatcher : IProtocolMatcher
    {
        public static Regex HTTP = new Regex("^(GET|POST|HEAD).*HTTP", RegexOptions.Compiled);
        private string decodedMessage;
        private byte[] message;

        private HttpEngine engine;
        public HttpEngine Engine
        {
            get
            {
                return engine;
            }
            set
            {
                engine = value;
            }
        }
        public bool IsBlockingProtocol
        {
            get
            {
                return true;
            }
        }
        public bool IsMatch(byte[] message)
        {
            this.message = message;
            decodedMessage = Encoding.UTF8.GetString(message);
            return HTTP.IsMatch(decodedMessage);
        }

        public void process()
        {
            engine.process(decodedMessage.Split(new string[]{Environment.NewLine},StringSplitOptions.None),message);
        }
    }
}
