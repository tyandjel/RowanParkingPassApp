using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ModServer
{
    /// <summary>
    /// An interface for a protocol matcher.
    /// Use protocol mathers to return true when a packet confirmed to be a matching protocol.
    /// </summary>
    public interface IProtocolMatcher
    {
        bool IsBlockingProtocol
        {
            get;
        }
        bool IsMatch(byte[] message);
        void process();
    }
}
