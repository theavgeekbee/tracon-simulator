package skill.issue.traconsim.sim.ticking;

import org.jetbrains.annotations.Nullable;

public class TickData {
    public final DataType dataType;
    public final Object data;

    public TickData(DataType dt, @Nullable Object data) {
        Object data1 = null;
        dataType = dt;
        switch (dt) {
            case TGT_HEADING_CHANGE, TGT_ALTITUDE_CHANGE, TGT_SPEED_CHANGE -> {
                if (data == null) dataTypeIncorrect("Number", "null", "null");
                if ( data instanceof Number )
                    data1 = data;
                else
                    dataTypeIncorrect("Number", data.getClass().getSimpleName(), data.toString());
            }
            case HANDOFF_INITIATE, POINTOUT_INITIATE -> {
                if (data == null) dataTypeIncorrect("String", "null", "null");
                if  ( data instanceof String )
                    data1 = data;
                else
                    dataTypeIncorrect("String", data.getClass().getSimpleName(), data.toString());
            }
        }

        this.data = data1;
    }
    private void dataTypeIncorrect(String expected, String received, String data) {
        throw new IllegalStateException("Invalid data type! Expected " + expected + ", received " + received + ", data " + data);
    }
}
