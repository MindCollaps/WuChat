package mindcollaps.lib.reqRes.res;

public class ResNotConnected extends Response{

    public ResNotConnected() {
        super(null);
    }

    @Override
    public int getHttpCode() {
        return 4001;
    }
}
