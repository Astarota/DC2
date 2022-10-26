    package org.example;
    import java.io.File;
    import java.io.IOException;
    import java.io.PrintWriter;
    import java.net.Socket;


    /**
     * Processor of HTTP request.
     */
    public class Processor<T> extends Thread {
        private final Socket socket;
        private final HttpRequest request;


        public Processor(Socket socket, HttpRequest request) {
            this.socket = socket;
            this.request = request;
        }
//        @Override
//        public void run(){
//            try {
//                process();
//            } catch (IOException e) {
//            throw new RuntimeException(e);
//            }
//        }


        static int getFactorial(int n)
        {
            int result = 1;

            if (n == 0) {

                return result;
            }
            if (n == 1) {

                return result;
            }


            if (n != 2) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            result = n * getFactorial(n-1);
            return result;
        }






        public void process() throws IOException {
            // Print request that we received.
            System.out.println("Got request:");
            System.out.println(request.toString());
            System.out.flush();

            // To send response back to the client.
            PrintWriter output = new PrintWriter(socket.getOutputStream());

            // We are returning a simple web page now.
            output.println("HTTP/1.1 200 OK");
            output.println("Content-Type: text/html; charset=utf-8");
            output.println();
            output.println("<html>");
            output.println("<head><title>Hello</title></head>");
            output.println("<body><p>Hello, world!</p></body>");
            output.println("</html>");
            output.flush();
            request(output);



            socket.close();
        }
        public void request(PrintWriter output) throws IOException {
            StringBuilder string = new StringBuilder(request.getRequestLine());
            System.out.println(string);
            string.delete(0,4);
            string.delete(string.length() - 9, string.length() + 1);
            System.out.println();
            System.out.println(string);
            System.out.println();

            if(string.toString().contains("/create/")) {
                string.delete(0,8);
                File value = new File(string.toString());
                try {
                    boolean new_value = value.createNewFile();
                    if (new_value) {

                        System.out.println("New Java File is created.");
                        output.println("<html>");
                        output.println("<body><p>" + string + " is created.</p></body>");
                    }
                    else {
                        System.out.println("The file already exists.");
                        output.println("<html>");
                        output.println("<body><p>" + string + " already exists.</p></body>");
                    }
                    output.println("</html>");
                    output.flush();
                }
                catch(Exception e) {
                    e.getStackTrace();
                }
            } else if (string.toString().contains("/exec/")) {
                string.delete(0,6);
                int number = Integer.parseInt(string.toString());
                System.out.println("Calculating Armstrong Number.");
                output.println("<html>");
                output.println("<body><p> Factorial of number " + string + "is " + getFactorial(number) + ".</p></body>");
                output.println("</html>");
                output.flush();
            }
        }
    }


