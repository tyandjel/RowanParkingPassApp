using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO; 

using ModServer;

namespace ConsoleApplication1
{
    class Program
    {
        const string SUCCESS_STR = "You have successfully logged into the Rowan University Central Authentication Service.<";
        const string SUCCESS_LOGOUT_STR = "You have successfully logged out of the Rowan University Central Authentication Service.";
        const string FAIL_STR = "NO";
        static void Main(string[] args)
        {
            Boolean logged_in = false;
            HttpBrowserEntitiy browser = new HttpBrowserEntitiy();
            // Secure connections use SSL/TLS
            HttpConnection CAS_Con = browser.openSecureConnection("cas.rowan.edu", HttpBrowserEntitiy.PORTS.SSL, "cas.rowan.edu");
            var request = new HttpRequest("/cas/login");
            SignLoginRequest(request);
            WriteToFile(request.ToString());
            var response = CAS_Con.sendRequest(request);
            browser.processResponse(response);
            browser.SessionCookies.Remove("Path");
            WriteToFile(response.ToString());
            WriteToFile("");
            string resp = response.ToString();
            string lp = GetMiddleString(resp,"name=\"lt\" value=\"","\" />");
            //WriteToFile(lp);
            //&execution = e4s1 & _eventId = submit & submit = LOGIN
            if (1 == 1)
            {
                request = new HttpRequest("POST", "/cas/login;jsessionid="+browser.SessionCookies["JSESSIONID"]);
                request.cookies = browser.SessionCookies;
                SignLoginRequest(request);
                request.addHeader("Content-Type", "application/x-www-form-urlencoded");
                //"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"
                Console.Write("Enter Rowan Username: ");
                request.addPost("username", Console.ReadLine());
                Console.Write("\r\nEnter Password(NOT HIDDEN): ");
                request.addPost("password", Console.ReadLine());
                request.addPost("lt", lp);
                request.addPost("execution", "e1s1");
                request.addPost("_eventId", "submit");
                request.addPost("submit", "LOGIN");
                request.guessContentLength();
                //WriteToFile(request.ToString());  LETS NOT SEE EACHOTHER PASSWORD YES?!
                response = CAS_Con.sendRequest(request);
                WriteToFile(response.ToString() + Environment.NewLine);
                browser.processResponse(response);
                browser.SessionCookies.Remove("Path");

                if(response.Body.Contains(SUCCESS_STR))
                {
                    showMessage("You have logged in successfully.");
                    logged_in = true;
                }
                else
                {
                    showMessage("Login Failure!");
                    logged_in = false;
                }
            }
            if (logged_in)
            {
                showMessage("Press any key to log out.");
            }
            else
            {
                showMessage("Press any key to end.");
            }
            Console.Read();
            if (logged_in)
            {
                request = new HttpRequest("/cas/logout");
                SignLoginRequest(request);
                request.cookies = browser.SessionCookies;
                WriteToFile(request.ToString() + Environment.NewLine);
                response = CAS_Con.sendRequest(request);
                WriteToFile(response.ToString());

                if (response.Body.Contains(SUCCESS_STR))
                {
                    showMessage("You have logged in successfully.");
                }
                else
                {
                    showMessage("Error loggin out. This is weird..");
                    Console.Read();
                }
            }
        }

        static void showMessage(string text)
        {
            Console.WriteLine("---\t"+text+"\t---");
        }

        static void SignLoginRequest(HttpRequest request)
        {
            request.addHeader("Host", "cas.rowan.edu");
            request.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        }

        static void WriteToFile(string wtr)
        {
            using (System.IO.StreamWriter file = File.AppendText("output.txt"))
            {
                Console.WriteLine(wtr);
                file.WriteLine(wtr);
            }
        }

        static string GetMiddleString(string input, string firsttoken, string lasttoken)
        {
            int pos1 = input.ToLower().IndexOf(firsttoken.ToLower()) + firsttoken.Length;
            int pos2 = input.ToLower().IndexOf(lasttoken.ToLower(),pos1);
            return input.Substring(pos1, pos2 - pos1);
        }
    }
}
