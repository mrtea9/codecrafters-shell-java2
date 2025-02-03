package terminal;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Structure;

import java.io.Closeable;
import java.util.Arrays;

public interface LibC extends Library {

    public LibC INSTANCE = Native.load("c", LibC.class);

    public final int STDIN_FILENO = 0;

    public final int NCCS = 32;

    public final int ICANON = 2;

    public final int ECHO = 8;

    public final int IGNCR = 128;

    public final int VTIME = 5;
    public final int VMIN = 6;

    public final int TCSANOW = 0;

    public final int ICRNL = 400;

    String strerror(int errnum);

    int tcgetattr(int fd, final struct_termios termios_p);

    int tcsetattr(int fd, int optional_actions, final struct_termios termios_p);

    @Structure.FieldOrder({
            "c_iflag",
            "c_oflag",
            "c_cflag",
            "c_lflag",
            "c_line",
            "c_cc",
            "c_ispeed",
            "c_ospeed",
    })
    public class struct_termios extends Structure implements Cloneable {

        public int c_iflag;

        public int c_oflag;

        public int c_cflag;

        public int c_lflag;

        public byte c_line;

        public byte[] c_cc = new byte[NCCS];

        public int c_ispeed;

        public int c_ospeed;

        @Override
        protected struct_termios clone() {
            final var copy = new struct_termios();

            copy.c_iflag = c_iflag;
            copy.c_oflag = c_oflag;
            copy.c_cflag = c_cflag;
            copy.c_lflag = c_lflag;
            copy.c_line = c_line;
            copy.c_cc = c_cc.clone();
            copy.c_ispeed = c_ispeed;
            copy.c_ospeed = c_ospeed;

            return copy;
        }

        @Override
        public String toString() {
            return "struct_termios[c_iflag=" + c_iflag + ", c_oflag=" + c_oflag + ", c_cflag=" + c_cflag + ", c_lflag=" + c_lflag + ", c_line=" + c_line + ", c_cc=" + Arrays.toString(c_cc) + ", c_ispeed=" + c_ispeed + ", c_ospeed=" + c_ospeed + "]";
        }
    }

}
