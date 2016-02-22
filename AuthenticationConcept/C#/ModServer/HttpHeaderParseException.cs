using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ModServer
{
    public class HttpHeaderParseException : Exception
    {
        public HttpHeaderParseException(String message) : base(message) { }
    }
}
