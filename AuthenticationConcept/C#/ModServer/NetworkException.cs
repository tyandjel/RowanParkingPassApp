using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net.Sockets;
using System.Net;

namespace ModServer
{
    public class NetworkException : Exception
    {
        //private TcpClient client;

        //public NetworkException(String message, TcpClient client)
        //{
        //    this.client = client;
        //}

        public NetworkException(String message) : base(message)
        {
            
        }

        //public TcpClient getTcpClient()
        //{
        //    return client;
        //}
    }
}
