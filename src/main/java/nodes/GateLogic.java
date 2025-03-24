package nodes;

import java.util.Arrays;

public class GateLogic {

    public String gateType;
    public int numInputs;
    public int opValue; // number of True/1's in the input
    public static final String[] GATE_SET = {"AND", "OR", "XOR", "NOT"};


    public GateLogic(String gateType, int numInputs) {

        gateType = gateType.toUpperCase().replace(" ", "");
        if (Arrays.asList(GATE_SET).contains(gateType)) { // Input validation for gateType
            this.gateType = gateType;
        } else {
            throw new RuntimeException("Invalid nodes Gate Type");
        }

        if (this.gateType.equals("NOT") && numInputs != 1) { // Input validation for numInputs depending on gate
            throw new RuntimeException("NOT gate was given more than 1 input");
        } else if (! this.gateType.equals("NOT") && numInputs < 2) {
            throw new RuntimeException("NumInputs was given a value less than 2 for non NOT gate");
        }
        this.numInputs = numInputs;
        this.opValue = 0;
    }

    public int getNumInputs() {
        return this.numInputs;
    }

    public String getGateType() {
        return this.gateType;
    }

    public void addInput() { // adds 1 to the number of True values; if this number is more than one in an XOR gate, the output is 0
        this.opValue++;
    }

    public int getOperationResult() { // gets the output of the gate given the current inputs
        if (this.gateType.equals("NOT")) {
            return (this.opValue == 0) ? 1 : 0;
        }
        if (this.gateType.equals("AND")) {
            return (this.opValue == this.numInputs) ? 1 : 0;
        }
        if (this.gateType.equals("OR")) {
            return (this.opValue > 0) ? 1 : 0;
        }
        if (this.gateType.equals("XOR")) {
            return (this.opValue > 1) ? 0 : 1;
        }
        throw new RuntimeException("IDK HOW U GOT HERE");
    }
}
