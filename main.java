import java.util.HashMap;
final int squareSize = 32; //should always be a power of 2, even though I'm not really sure why.
HashMap<int, Tromino> indexMapping;
int[][] grid;
Coord emptySlot;
public class Coord{
	public int x;
	public int y;
	Coord(int x , int y){ this.x = x; this.y = y; }
}
public class Color {
	Color(int r, int g, int b){
		this.r = r; this.g = g; this.b = b;
	}
	public int r; public int g; public int b;
}
public class Tromino {
	public Coord[] locations;
	public Coord offset;
	public Color color;
	public Tromnio(Coord[] locations){
		this.locations = locations;
		this.offset = new Coord(squareSize, squareSize);
		this.color = new Color(randInRange(0, 255),randInRange(0, 255),randInRange(0, 255));
	}
	public Tromino(Coord[] locations, Coord offset){
		this.locations = locations;
		this.offset = offset;
		this.color = new Color(randInRange(0, 255),randInRange(0, 255),randInRange(0, 255));
	}
	void draw(){
		fill(color(this.color.r, this.color.g, this.color.b));
		rect(locations[0].x*offset.x, locations[0].y*offset.y);
		rect(locations[1].x*offset.x, locations[1].y*offset.y);
		rect(locations[2].x*offset.x, locations[2].y*offset.y);
	}
}
Coord rotRight(Coord a, int sz){
	return new Coord(Math.abs(a.y-(sz-1), a.x));
}
Coord rotLeft(Coord a, int sz){
	return new Coord(a.y, Math.abs(a.x-(sz-1)));
}

boolean complete(int[][] g){
	int empties = 0;
	for(int y = 0; y < g.length; y++){
		for(int x = 0; x < g[y].length; x++){
			if(g[y][x] == 0)
				empties++;
			if(empties >= 3)
				return false;
		}
	}
	return true;
}
enum Quadrant {
	TL, TR, BL, BR;
}
Quadrant inQuadrant(Coord x, Coord offset, int sz){
	if(x.x < offset.x + sz/2 && x.y < offset.y + sz/2)
		return Quadrant.TL;
	if(x.x < offset.x + sz/2 && x.y >= offset.y + sz/2)
		return Quadrant.TR;
	if(x.x >= offset.x + sz/2 && x.y < offset.y + sz/2)
		return Quadrant.BL
	if(x.x >= offset.x + sz/2 && x.y >= offset.y + sz/2)
		return Quadrant.BR;
}
void rec(int[][] g, int sz, Coord top){
	if(sz == 2){
		Coord[] newCoord = new Coord[3];
		int iter = 0;
		for(int x = 0; x < sz; x++){
			for(int z = 0; z < sz; z++){
				if(g[top.x+x][top.y+z] == 0){
					g[top.x+x][top.y+z] = indexMapping.size()+1;
					newCoord[iter] = new Coord(top.x+x, top.y+z);
					iter++;
				}
			}
		}
		indexMapping[indexMapping.size()+1] = new Tromino(newCoord);
	} else {
		Coord missing = new Coord(top.x, top.y);
		for(int x= top.x; x < top.x + sz; x++){
			for(int y = top.y; y < top.y+sz){
				if(grid[x][y] != 0){
					missing.x = x;
					missing.y = y;
				}
			}
		}
		if(inQuadrant(missing,top,sz) == Quadrant.TL){
			rec(g, sz/2, top);
			Coord[] newTrom = new Coord[3];
			newTrom[0] = new Coord(top.x+sz/2,top.y+sz/2-1);
			g[top.x+sz/2][top.y+sz/2-1] = indexMapping.size()-1;
			newTrom[1] = new Coord(top.x+sz/2,top.y+sz/2);
			g[top.x+sz/2][top.y+sz/2] = indexMapping.size()-1;
			newTrom[2] = new Coord(top.x+sz/2-1,top.y+sz/2);
			g[top.x+sz/2-1][top.y+sz/2] = indexMapping.size()-1;
			
			indexMapping[indexMapping.size()-1] = new Tromino(newTrom);
			
			rec(g, sz/2, new Coord(top.x, top.y+sz/2));
			rec(g, sz/2, new Coord(top.x+sz/2, top.y+sz));
			rec(g, sz/2, new Coord(top.x+sz/2, top.y+sz/2));
		} else if(inQuadrant(missing, top, sz) == Quadrant.TR){
			rec(g, sz/2, new Coord(top.x, top.y+sz/2));
			
			newTrom[0] = new Coord(top.x+sz/2,top.y+sz/2-1);
			g[top.x+sz/2][top.y+sz/2-1] = indexMapping.size()-1;
			newTrom[1] = new Coord(top.x+sz/2,top.y+sz/2);
			g[top.x+sz/2][top.y+sz/2] = indexMapping.size()-1;
			newTrom[2] = new Coord(top.x+sz/2-1,top.y+sz/2);
			g[top.x+sz/2-1][top.y+sz/2] = indexMapping.size()-1;
			indexMapping[indexMapping.size()-1] = new Tromino(newTrom);
			
			rec(g, sz/2, top);
			rec(g, sz/2, new Coord(top.x+sz/2, top.y));
			rec(g, sz/2, new Coord(top.x+sz/2, top.y+sz/2));
		} else if(inQuadrant(missing, top, sz) == Quadrant.BL){
			rec(g, sz/2, new Coord(top.x+sz/2, top.y));
			
			newTrom[0] = new Coord(top.x+sz/2,top.y+sz/2-1);
			g[top.x+sz/2][top.y+sz/2-1] = indexMapping.size()-1;
			newTrom[1] = new Coord(top.x+sz/2,top.y+sz/2);
			g[top.x+sz/2][top.y+sz/2] = indexMapping.size()-1;
			newTrom[2] = new Coord(top.x+sz/2-1,top.y+sz/2);
			g[top.x+sz/2-1][top.y+sz/2] = indexMapping.size()-1;
			indexMapping[indexMapping.size()-1] = new Tromino(newTrom);
			
			rec(g, sz/2, top);
			rec(g, sz/2, new Coord(top.x, top.y+sz/2));
			rec(g, sz/2, new Coord(top.x+sz/2, top.y+sz/2));
		} else {
			rec(g, sz/2, new Coord(top.x+sz/2, top.y+sz/2));
			
			newTrom[0] = new Coord(top.x+sz/2,top.y+sz/2-1);
			g[top.x+sz/2][top.y+sz/2-1] = indexMapping.size()-1;
			newTrom[1] = new Coord(top.x+sz/2,top.y+sz/2);
			g[top.x+sz/2][top.y+sz/2] = indexMapping.size()-1;
			newTrom[2] = new Coord(top.x+sz/2-1,top.y+sz/2);
			g[top.x+sz/2-1][top.y+sz/2] = indexMapping.size()-1;
			indexMapping[indexMapping.size()-1] = new Tromino(newTrom);
			
			rec(g, sz/2, new Coord(top.x+sz/2, top.y));
			rec(g, sz/2, new Coord(top.x, top.y+sz/2));
			rec(g, sz/2, top);
		}
	}
}
void setup(){
	int wx = 16; //Grid X -- Modify these for different combinations, power of 2
	int wy = 16; //Grid Y -- see above
	background(white);
	
	emptySlot = new Coord(3,4); //Empty slot. Has to be one.
	
	grid = new int[wx][wy];
	grid[emptySlot.y][emptySlot.x] = -1; // empty slot ID
	indexMapping = new HashMap<int, Tromino>();
	
	rec(grid, grid.length, new Coord(0,0));
	
	size(wx*squareSize, wy*squareSize);
}
void draw(){
	for(int x = 1; x <= indexMapping.size(); x++){
		indexMapping[x].draw();
	}
}