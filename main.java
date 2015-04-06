import java.util.HashMap;
import java.util.Random;
class Coord {
  public int x;
  public int y;
  Coord(int x , int y){ this.x = x; this.y = y; }
}
class RGB_Color {
  public int r; public int g; public int b;
  RGB_Color(int r, int g, int b){
    this.r = r; this.g = g; this.b = b;
  }
  String toString(){
    return "" + r + "," + g +"," +b;
  }

}
int randInRange(int min, int max){
  Random rand = new Random();
  return rand.nextInt((max-min)+1)+min;
}
public class Tromino {
  public Coord[] locations;
  public Coord offset;
  public RGB_Color rgbcolor;
 Tromino(Coord[] locations){
    this.locations = locations;
    this.offset = new Coord(squareSize, squareSize);
    this.rgbcolor = new RGB_Color(randInRange(25, 255),randInRange(25, 225),randInRange(25, 225));
  }
  Tromino(Coord[] locations, Coord offset){
    this.locations = locations;
    this.offset = offset;
    this.rgbcolor = new RGB_Color(randInRange(25, 225),randInRange(25, 225),randInRange(25, 225));
  }
  void draw(){
    fill(color(this.rgbcolor.r, this.rgbcolor.g, this.rgbcolor.b));
    rect((float)locations[0].x*offset.x, (float)locations[0].y*offset.y, (float)squareSize,(float)squareSize);
    rect((float)locations[1].x*offset.x, (float)locations[1].y*offset.y, (float)squareSize,(float)squareSize);
    rect((float)locations[2].x*offset.x, (float)locations[2].y*offset.y, (float)squareSize,(float)squareSize);
  }
}
Coord rotRight(Coord a, int sz){
  return new Coord(Math.abs(a.y-(sz-1)), a.x);
}
Coord rotLeft(Coord a, int sz){
  return new Coord(a.y, Math.abs(a.x-(sz-1)));
}
static final int squareSize = 32; //should always be a power of 2, even though I'm not really sure why.
public static HashMap<Integer, Tromino> indexMapping;
int[][] grid;
Coord emptySlot;
boolean complete(int[][] g){
  int empties = 0;
  for(int y = 0; y < g.length; y++){
    for(int x = 0; x < g[y].length; x++){
      if(g[y][x] == 0)
        empties++;
      if(empties > 1)
        return false;
    }
  }
  return true;
}

Quadrant inQuadrant(Coord x, Coord offset, int sz){
  if(x.x < offset.x + sz/2 && x.y < offset.y + sz/2)
    return Quadrant.TL;
  if(x.x < offset.x + sz/2 && x.y >= offset.y + sz/2)
    return Quadrant.TR;
  if(x.x >= offset.x + sz/2 && x.y < offset.y + sz/2)
    return Quadrant.BL;
  if(x.x >= offset.x + sz/2 && x.y >= offset.y + sz/2)
    return Quadrant.BR;
   return Quadrant.TL;
}
void rec(int[][] g, int sz, Coord top){
  if(sz == 2){
    Coord[] toAdd = new Coord[3];
    int iter = 0;
    for(int x = 0; x < sz; x++){
      for(int z = 0; z < sz; z++){
        if(g[top.x+x][top.y+z] == 0){
           g[top.x+x][top.y+z] = indexMapping.size()+1;
           if(iter < 3){
             toAdd[iter] = new Coord(top.x+x, top.y+z);
             iter++;
           }
        }
      }
    }
    indexMapping.put(indexMapping.size()+1;, new Tromino(toAdd));
  } else {
    Coord missing = new Coord(top.x, top.y);
    for(int x= top.x; x < top.x + sz; x++){
      for(int y = top.y; y < top.y+sz; y++){
        if(grid[x][y] != 0){
          missing.x = x;
          missing.y = y;
        }
      }
    }
    Coord[] newTrom = new Coord[3];
    if(inQuadrant(missing,top,sz) == Quadrant.TL){
      rec(g, sz/2, top);
     
      newTrom[0] = new Coord(top.x+sz/2,top.y+sz/2-1);
      g[top.x+sz/2][top.y+sz/2-1] = indexMapping.size()+1;
      newTrom[1] = new Coord(top.x+sz/2,top.y+sz/2);
      g[top.x+sz/2][top.y+sz/2] = indexMapping.size()+1;
      newTrom[2] = new Coord(top.x+sz/2-1,top.y+sz/2);
      g[top.x+sz/2-1][top.y+sz/2] = indexMapping.size()+1;
      
      indexMapping.put(indexMapping.size()+1;, new Tromino(newTrom));
      
      rec(g, sz/2, new Coord(top.x, top.y+sz/2));
      rec(g, sz/2, new Coord(top.x+sz/2, top.y));
      rec(g, sz/2, new Coord(top.x+sz/2, top.y+sz/2));
    } else if(inQuadrant(missing, top, sz) == Quadrant.TR){
      rec(g, sz/2, new Coord(top.x, top.y+sz/2));
      
      newTrom[0] = new Coord(top.x+sz/2,top.y+sz/2-1);
      g[top.x+sz/2][top.y+sz/2-1] = indexMapping.size()+1;
      newTrom[1] = new Coord(top.x+sz/2,top.y+sz/2);
      g[top.x+sz/2][top.y+sz/2] = indexMapping.size()+1;
      newTrom[2] = new Coord(top.x+sz/2-1,top.y+sz/2-1);
      g[top.x+sz/2-1][top.y+sz/2-1] = indexMapping.size()+1;
      indexMapping.put(indexMapping.size()+1;, new Tromino(newTrom));
      
      rec(g, sz/2, top);
      rec(g, sz/2, new Coord(top.x+sz/2, top.y));
      rec(g, sz/2, new Coord(top.x+sz/2, top.y+sz/2));
    } else if(inQuadrant(missing, top, sz) == Quadrant.BL){
      rec(g, sz/2, new Coord(top.x+sz/2, top.y));
      
      newTrom[0] = new Coord(top.x+sz/2-1,top.y+sz/2);
      g[top.x+sz/2-1][top.y+sz/2] = indexMapping.size()+1;
      newTrom[1] = new Coord(top.x+sz/2,top.y+sz/2);
      g[top.x+sz/2][top.y+sz/2] = indexMapping.size()+1;
      newTrom[2] = new Coord(top.x+sz/2-1,top.y+sz/2-1);
      g[top.x+sz/2-1][top.y+sz/2-1] = indexMapping.size()+1;
      indexMapping.put(indexMapping.size()+1;, new Tromino(newTrom));
      
      rec(g, sz/2, top);
      rec(g, sz/2, new Coord(top.x, top.y+sz/2));
      rec(g, sz/2, new Coord(top.x+sz/2, top.y+sz/2));
    } else {
      rec(g, sz/2, new Coord(top.x+sz/2, top.y+sz/2));
      //checked
      newTrom[0] = new Coord(top.x+sz/2-1,top.y+sz/2);
      g[top.x+sz/2-1][top.y+sz/2] = indexMapping.size()+1;
      newTrom[1] = new Coord(top.x+sz/2,top.y+sz/2-1);
      g[top.x+sz/2][top.y+sz/2-1] = indexMapping.size()+1;
      newTrom[2] = new Coord(top.x+sz/2-1,top.y+sz/2-1);
      g[top.x+sz/2-1][top.y+sz/2-1] = indexMapping.size()+1;
      indexMapping.put(indexMapping.size()+1;, new Tromino(newTrom));
      
      rec(g, sz/2, new Coord(top.x+sz/2, top.y));
      rec(g, sz/2, new Coord(top.x, top.y+sz/2));
      rec(g, sz/2, top);
    }
  }
}
void setup(){
  int wx = 8; //Grid X -- Modify these for different combinations, power of 2
  int wy = 8; //Grid Y -- see above
  background(255);
  
  emptySlot = new Coord(4,4); //Empty slot. Has to be one.
  
  grid = new int[wx][wy];
  grid[emptySlot.y][emptySlot.x] = MIN_INT; // empty slot ID
  indexMapping = new HashMap<Integer, Tromino>();
  
  rec(grid, grid.length, new Coord(0,0));
  
  size(wx*squareSize, wy*squareSize);
  println(complete(grid));
  println(grid[4][3]);
  println(grid[3][7]);
  println(indexMapping.get(grid[3][7]).rgbcolor);
}
boolean t = true;
void draw(){
  clear();
  for(int x = 1; x <= indexMapping.size(); x++){
    indexMapping.get(x).draw();
  }
  fill(0);
}