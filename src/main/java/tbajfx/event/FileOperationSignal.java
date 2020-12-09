package tbajfx.event;

public final class FileOperationSignal extends Signal {

    private FileOperationType type;
    
    public FileOperationSignal(FileOperationType type)
    {
        this.type = type;
    }

    public FileOperationType getType() {
        return type;
    }

    public void setType(FileOperationType type) {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + " { type: " + type.name() + " }";
    }

}
