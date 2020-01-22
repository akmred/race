public abstract class Stage {
    protected int length;
    protected String description;
    protected  boolean isTunnel;

    public String getDescription() {
        return description;
    }

    public abstract void go(Car c);
}
