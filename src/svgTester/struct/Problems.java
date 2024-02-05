package svgTester.struct;

/**
 * Enum of Problems that could appear in the network.
 */
public enum Problems {
    DELAY(1),
    BLOCKED(3),
    STATION_WORK(2),
    REPLACED(4),
    ENDED(10),
    NULL(0);

    private int value;

    private Problems(int value) {
        this.value = value;
    }
    public int getValue() {
        return this.value;
    }
}