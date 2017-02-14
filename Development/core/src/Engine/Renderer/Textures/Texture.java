////////////////////////////////////////////////
// The Pyramid's Curse
// A game by Chris Dalke
////////////////////////////////////////////////
// File: Texture.java
////////////////////////////////////////////////

package Engine.Renderer.Textures;

//java package imports
////////////////////////////////////////////////
// Main class
////////////////////////////////////////////////

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Texture {

    //A unique id (just uses the filename for now)
    public String uuid;
    public com.badlogic.gdx.graphics.Texture texID;
    public TextureRegion[] regions;
    public int numRegions;
    public int width;
    public int height;
    public boolean hasTransparency;
    public String filename;

    public com.badlogic.gdx.graphics.Texture getTex(){
        return texID;
    }

    public TextureRegion getRegion(){
        return regions[0];
    }

    public TextureRegion getRegion(int region){
        try {
            return regions[region];
        } catch (ArrayIndexOutOfBoundsException e){
            return null;
        }
    }

    public int getNumRegions(){
        return numRegions;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean getHasTransparency() {
        return hasTransparency;
    }

    public void setHasTransparency(boolean hasTransparency) {
        this.hasTransparency = hasTransparency;
    }

    public Texture(){
        //do nothing on object construction
    }

    public void setID(com.badlogic.gdx.graphics.Texture id){
        texID = id;
    }

    public com.badlogic.gdx.graphics.Texture getID(){
        return texID;
    }

    public void cleanup(){
        if (texID != null) {
            texID.dispose();
            regions = null;
        }
    }

    public void setHasFiltering(boolean hasFiltering){
        if (hasFiltering){
            texID.setFilter(com.badlogic.gdx.graphics.Texture.TextureFilter.Linear, com.badlogic.gdx.graphics.Texture.TextureFilter.Linear);
        } else {
            texID.setFilter(com.badlogic.gdx.graphics.Texture.TextureFilter.Nearest, com.badlogic.gdx.graphics.Texture.TextureFilter.Nearest);
        }
    }

    public void setWrap(boolean hasWrap){
        if  (hasWrap){
            texID.setWrap(com.badlogic.gdx.graphics.Texture.TextureWrap.Repeat, com.badlogic.gdx.graphics.Texture.TextureWrap.Repeat);
        } else {
            texID.setWrap(com.badlogic.gdx.graphics.Texture.TextureWrap.ClampToEdge, com.badlogic.gdx.graphics.Texture.TextureWrap.ClampToEdge);

        }
    }


}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////