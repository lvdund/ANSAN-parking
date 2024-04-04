package socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class TagClient
{
    public static void main( String[] args )
    {
        String server = "127.0.0.1";
        int port = 5529;
        String path = "/";

        System.out.println( "Loading contents of URL: " + server );

        try
        {
            // Connect to the server
          //  Socket socket = new Socket( server, 80 );
        	Socket socket = new Socket( server, port );
            // Create input and output streams to read from and write to the server
            PrintStream out = new PrintStream( socket.getOutputStream() );
            BufferedReader in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
            

            String payload =  "@V2X,1,1,000000000000001,21.19254315,105.82550243,10/5/21,6:27:49,1";
            
            System.out.println("-------> TCP body: \n" + payload);
            
            out.println(payload);
            out.println();

            // Read data from the server until we finish reading the document
            String line = in.readLine();
            
            System.out.println("-------> Response: ");
         
            while( line != null )
            {
                System.out.println( line );
                line = in.readLine();
            }

            // Close our streams
            in.close();
            out.close();
            socket.close();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }
}