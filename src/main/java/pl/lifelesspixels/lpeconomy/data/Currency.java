package pl.lifelesspixels.lpeconomy.data;

public class Currency {

    private final String identifier;
    private final String readableName;
    private final long defaultBalance;

    public Currency(String identifier, String readableName, long defaultBalance) {
        this.identifier = identifier;
        this.readableName = readableName;

        if(defaultBalance < 0)
            throw new IllegalArgumentException("default balance cannot be negative");
        this.defaultBalance = defaultBalance;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getReadableName() {
        return readableName;
    }

    public long getDefaultBalance() {
        return defaultBalance;
    }
}
