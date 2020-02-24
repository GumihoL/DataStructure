package byow.Core;

public class Position {
    private Integer x;
    private Integer y;

    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }
    public int getX(){return x;}
    public int getY(){return y;}
    // 返回在this Postion左边的紧邻的Position
    public Position left(){
        return new Position(x - 1, y);
    }
    // 返回在this Postion右边的紧邻的Position
    public Position right(){
        return new Position(x + 1, y);
    }
    // 返回在this Postion上边的紧邻的Position
    public Position above(){
        return new Position(x, y + 1);
    }
    // 返回在this Postion下边的紧邻的Position
    public Position below(){
        return new Position(x, y - 1);
    }

    public Position southWestTranslate(int deltaX, int deltaY){
        return new Position(this.x - deltaX, this.y - deltaY);
    }

    public Position northTranslate(int deltaY){
        return new Position(this.x, this.y + deltaY);
    }

    public Position westTranslate(int deltaX){
        return new Position(this.x - deltaX, this.y);
    }

    public Position eastTranslate(int deltaX){
        return new Position(this.x + deltaX, this.y);
    }

    public Position southTranslate(int deltaY){
        return new Position(this.x, this.y - deltaY);
    }

    @Override
    public String toString(){
        return new String("(x = " + x + ", y = " + y + ")");
    }

    @Override
    public int hashCode(){
        return x.hashCode() + y.hashCode();
    }

    @Override
    public boolean equals(Object other){
        if(other == null) return false;
        if(other.getClass() != getClass()) return false;
        Position o = (Position) other;
        return x == o.x && y == o.y;
    }

}
