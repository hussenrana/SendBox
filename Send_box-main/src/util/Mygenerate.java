package util;
//this class for getting and setting the details of generate table
public class Mygenerate {
private int ID;
private String type;
private String username;
private int width;
private int height;
private Cell cell;
private int StartX,StartY,EndX,EndY;

public Mygenerate(int iD, String type, String username, int width, int height, Cell cell,int StartX,int StartY,int EndX,int EndY
) {
	ID = iD;
	this.type = type;
	this.username = username;
	this.width = width;
	this.height = height;
	this.cell = cell;
	this.StartX=StartX;
	this.StartY=StartY;
	this.EndX=EndX;
	this.EndY=EndY;
}
public int getStartX() {
	return StartX;
}
public void setStartX(int startX) {
	StartX = startX;
}
public int getStartY() {
	return StartY;
}
public void setStartY(int startY) {
	StartY = startY;
}
public int getEndX() {
	return EndX;
}
public void setEndX(int endX) {
	EndX = endX;
}
public int getEndY() {
	return EndY;
}
public void setEndY(int endY) {
	EndY = endY;
}
public int getID() {
	return ID;
}
public void setID(int iD) {
	ID = iD;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
public int getWidth() {
	return width;
}
public void setWidth(int width) {
	this.width = width;
}
public int getHeight() {
	return height;
}
public void setHeight(int height) {
	this.height = height;
}
public Cell getCell() {
	return cell;
}
public void setCell(Cell cell) {
	this.cell = cell;
}

public String toString() {
return (ID+" "+type+" "+username+" "+width+" "+height+" "+cell.toString());
		}
}
