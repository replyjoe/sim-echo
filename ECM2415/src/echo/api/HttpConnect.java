package echo.api;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;

/**
 * HTTPConnect class provides the functionality of setting up an HTTP connection.
 *
 * @author David Wakeling
 * @author 650033777
 * @author 620007467
 */
public class HttpConnect {
  final static int TIMEOUT  = 10000; /* ms  */
  final static int BUFFSIZE = 4096; /* 4KB */

  /**
   * httpConnect sets up a HTTP connection
   * @param method
   * @param url the URL to send the HTTP request to
   * @param headers the headers of the HTTP request
   * @param body the body of the HTTP request
   * @return a byte array of the HTTP connection bytes
   */
  public static byte[] httpConnect
    ( String      method
    , String      url
    , String[][]  headers
    , byte[]      body
    ) {

    try {
      /*
       * Setup connection.
       */
      URL u = new URL( url );
      HttpURLConnection conn = (HttpURLConnection) u.openConnection();

      conn.setRequestMethod( method );
      conn.setDoInput ( true );
      conn.setDoOutput( true );
      conn.setConnectTimeout( TIMEOUT );
      conn.setReadTimeout   ( TIMEOUT );
      for ( int i = 0; i < headers.length; i++ ) {
        conn.setRequestProperty( headers[ i ][ 0 ], headers[ i ][ 1 ] );
      }
      conn.connect();

      /*
       * Send data.
       */
      if(body != null) {
        DataOutputStream dos = new DataOutputStream( conn.getOutputStream() );
        dos.write( body );
        dos.flush();
        dos.close(); 
      }

      /*
       * Receive data.
       */
      DataInputStream       dis = new DataInputStream( conn.getInputStream() );
      ByteArrayOutputStream bos = new ByteArrayOutputStream();

      byte[] buffer = new byte[ BUFFSIZE ];
      for (;;) {
        int n = dis.read( buffer );
        if ( n > 0 ) {
          bos.write( buffer, 0, n );
        } else { 
          break;
        }
      }

      byte response[] = bos.toByteArray();
      dis.close();

      /*
       * Teardown connection.
       */
      conn.disconnect();

      return response;
    } catch ( IOException ex ) {
      System.err.println( ex ); 
      return null;
    }
  }
    
}
