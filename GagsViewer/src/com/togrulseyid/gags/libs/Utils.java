package com.togrulseyid.gags.libs;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
  
public class Utils {
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes = new byte[buffer_size];
            for(;;)
            {
              int count = is.read(bytes, 0, buffer_size);
              if(count == -1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
}

class PlurkInputStream2 extends FilterInputStream {

    protected PlurkInputStream2(InputStream in) {
        super(in);
    }

    @Override
    public int read(byte[] buffer, int offset, int count)
        throws IOException {
        int ret = super.read(buffer, offset, count);
        for ( int i = 2; i < buffer.length; i++ ) {
            if ( buffer[i - 2] == 0x2c && buffer[i - 1] == 0x05 && buffer[i] == 0 ) {
                buffer[i - 1] = 0;
            }
        }
        return ret;
    }

}