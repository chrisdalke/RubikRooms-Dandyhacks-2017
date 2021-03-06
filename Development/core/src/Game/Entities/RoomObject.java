////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: BackgroundCube
////////////////////////////////////////////////

package Game.Entities;

import Engine.Game.Entity.GameObject3d;
import Engine.Physics.KinematicPhysicsEntity;
import Game.Model.Room;
import Game.Model.Wall;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCompoundShape;

public class RoomObject extends GameObject3d {

    private Room roomDataObject;
    private KinematicPhysicsEntity roomPhysicsObject;

    public Room getRoomDataObject() {
        return roomDataObject;
    }

    public KinematicPhysicsEntity getRoomPhysicsObject() {
        return roomPhysicsObject;
    }

    //Each room object is 15mx15mx15m centered at 7.5, 7.5, 7.5
    public static final float ROOM_DIAMETER = 15f;
    public static final float ROOM_RADIUS = ROOM_DIAMETER/2;
    public static final float ROOM_WALL_THICKNESS = 0.5f;
    public static final float ROOM_RADIUS_CENTER = ROOM_DIAMETER/2 - ROOM_WALL_THICKNESS/2;
    public static final float ROOM_RADIUS_INNER = ROOM_DIAMETER/2 - ROOM_WALL_THICKNESS;
    public static final float ROOM_DIAMETER_INNER = ROOM_DIAMETER - ROOM_WALL_THICKNESS*2;

    public static void buildRoomBox(MeshPartBuilder meshBuilder, btCompoundShape collisionShape, float x, float y, float z, float sx, float sy, float sz){
        BoxShapeBuilder.build(meshBuilder,x,y,z,sx,sy,sz);
        btBoxShape box = new btBoxShape(new Vector3(sx/2.0f,sy/2.0f,sz/2.0f));
        Matrix4 transform = new Matrix4().translate(x,y,z);
        builtShape.addChildShape(transform,box);
    }

    public static Model builtModel;
    public static btCompoundShape builtShape;

    public static void buildRoomModel(Room roomDataObject){

        builtShape = new btCompoundShape();

        //Build a visual model and collision shape based on the room Data Object
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        Material mat = new Material();
        mat.set(ColorAttribute.createDiffuse(191.0f/255.0f * 0.5f, 245.0f/255.0f * 0.5f, 255.0f/255.0f * 0.5f,1.0f));
        Material matWalls = new Material();
        matWalls.set(ColorAttribute.createDiffuse(1.0f,1.0f,1.0f,1.0f));

        Material matGlassWalls = new Material();
        matGlassWalls.set(ColorAttribute.createDiffuse(0.7f,0.8f,1.0f,1.0f));
        matGlassWalls.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, 0.3f));

        matWalls.set(ColorAttribute.createDiffuse(1.0f,0.3f,0.4f,1.0f));
        matWalls.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, 0.8f));

        Material matFloors = new Material();
        matFloors.set(ColorAttribute.createDiffuse(1.0f,1.0f,1.0f,1.0f));

        Material matDoors = new Material();
        matDoors.set(ColorAttribute.createDiffuse(0f,0f,0f,1.0f));

        Node node1 = modelBuilder.node();
        node1.id = "borders";

        MeshPartBuilder meshBuilder = modelBuilder.part("borders", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, mat);

        //X
        if (roomDataObject.north.wallType != Wall.WALL_TYPE.NONE |  roomDataObject.floor.wallType != Wall.WALL_TYPE.NONE) {
            buildRoomBox(meshBuilder, builtShape, 0, -ROOM_RADIUS_CENTER, -ROOM_RADIUS_CENTER, ROOM_DIAMETER, ROOM_WALL_THICKNESS, ROOM_WALL_THICKNESS);
        }

        if (roomDataObject.south.wallType != Wall.WALL_TYPE.NONE |  roomDataObject.floor.wallType != Wall.WALL_TYPE.NONE) {
            buildRoomBox(meshBuilder, builtShape, 0, -ROOM_RADIUS_CENTER, ROOM_RADIUS_CENTER, ROOM_DIAMETER, ROOM_WALL_THICKNESS, ROOM_WALL_THICKNESS);
        }

        if (roomDataObject.north.wallType != Wall.WALL_TYPE.NONE |  roomDataObject.ceiling.wallType != Wall.WALL_TYPE.NONE) {
            buildRoomBox(meshBuilder, builtShape, 0, ROOM_RADIUS_CENTER, -ROOM_RADIUS_CENTER, ROOM_DIAMETER, ROOM_WALL_THICKNESS, ROOM_WALL_THICKNESS);
        }

        if (roomDataObject.south.wallType != Wall.WALL_TYPE.NONE |  roomDataObject.ceiling.wallType != Wall.WALL_TYPE.NONE) {
            buildRoomBox(meshBuilder, builtShape, 0, ROOM_RADIUS_CENTER, ROOM_RADIUS_CENTER, ROOM_DIAMETER, ROOM_WALL_THICKNESS, ROOM_WALL_THICKNESS);
        }

        //Y
        if (roomDataObject.north.wallType != Wall.WALL_TYPE.NONE |  roomDataObject.west.wallType != Wall.WALL_TYPE.NONE) {
            buildRoomBox(meshBuilder, builtShape, -ROOM_RADIUS_CENTER, 0, -ROOM_RADIUS_CENTER, ROOM_WALL_THICKNESS, ROOM_DIAMETER, ROOM_WALL_THICKNESS);
        }

        if (roomDataObject.south.wallType != Wall.WALL_TYPE.NONE |  roomDataObject.west.wallType != Wall.WALL_TYPE.NONE) {
            buildRoomBox(meshBuilder, builtShape, -ROOM_RADIUS_CENTER, 0, ROOM_RADIUS_CENTER, ROOM_WALL_THICKNESS, ROOM_DIAMETER, ROOM_WALL_THICKNESS);
        }

        if (roomDataObject.north.wallType != Wall.WALL_TYPE.NONE |  roomDataObject.east.wallType != Wall.WALL_TYPE.NONE) {
            buildRoomBox(meshBuilder, builtShape, ROOM_RADIUS_CENTER, 0, -ROOM_RADIUS_CENTER, ROOM_WALL_THICKNESS, ROOM_DIAMETER, ROOM_WALL_THICKNESS);
        }

        if (roomDataObject.south.wallType != Wall.WALL_TYPE.NONE |  roomDataObject.east.wallType != Wall.WALL_TYPE.NONE) {
            buildRoomBox(meshBuilder, builtShape, ROOM_RADIUS_CENTER, 0, ROOM_RADIUS_CENTER, ROOM_WALL_THICKNESS, ROOM_DIAMETER, ROOM_WALL_THICKNESS);
        }

        //Z
        if (roomDataObject.floor.wallType != Wall.WALL_TYPE.NONE |  roomDataObject.west.wallType != Wall.WALL_TYPE.NONE) {
            buildRoomBox(meshBuilder, builtShape, -ROOM_RADIUS_CENTER, -ROOM_RADIUS_CENTER, 0, ROOM_WALL_THICKNESS, ROOM_WALL_THICKNESS, ROOM_DIAMETER);
        }

        if (roomDataObject.floor.wallType != Wall.WALL_TYPE.NONE |  roomDataObject.east.wallType != Wall.WALL_TYPE.NONE) {
            buildRoomBox(meshBuilder, builtShape, ROOM_RADIUS_CENTER, -ROOM_RADIUS_CENTER, 0, ROOM_WALL_THICKNESS, ROOM_WALL_THICKNESS, ROOM_DIAMETER);
        }

        if (roomDataObject.ceiling.wallType != Wall.WALL_TYPE.NONE |  roomDataObject.west.wallType != Wall.WALL_TYPE.NONE) {
            buildRoomBox(meshBuilder, builtShape, -ROOM_RADIUS_CENTER, ROOM_RADIUS_CENTER, 0, ROOM_WALL_THICKNESS, ROOM_WALL_THICKNESS, ROOM_DIAMETER);
        }

        if (roomDataObject.ceiling.wallType != Wall.WALL_TYPE.NONE |  roomDataObject.east.wallType != Wall.WALL_TYPE.NONE) {
            buildRoomBox(meshBuilder, builtShape, ROOM_RADIUS_CENTER, ROOM_RADIUS_CENTER, 0, ROOM_WALL_THICKNESS, ROOM_WALL_THICKNESS, ROOM_DIAMETER);
        }

        //Walls, if walls exist in the level
        //Check all 6 possible walls

        Node node2 = modelBuilder.node();
        node2.id = "walls";
        MeshPartBuilder meshBuilderWalls = modelBuilder.part("walls", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, matWalls);
        //Build walls

        //Put down the normal walls
        if (roomDataObject.east.wallType != Wall.WALL_TYPE.NONE){
            if (roomDataObject.east.wallType != Wall.WALL_TYPE.WALL_GLASS){
                buildRoomBox(meshBuilderWalls,builtShape,ROOM_RADIUS_CENTER,0,0,ROOM_WALL_THICKNESS,ROOM_DIAMETER_INNER,ROOM_DIAMETER_INNER);
            }
        }
        if (roomDataObject.west.wallType != Wall.WALL_TYPE.NONE){
            if (roomDataObject.west.wallType != Wall.WALL_TYPE.WALL_GLASS){
                buildRoomBox(meshBuilderWalls,builtShape,-ROOM_RADIUS_CENTER,0,0,ROOM_WALL_THICKNESS,ROOM_DIAMETER_INNER,ROOM_DIAMETER_INNER);
            }
        }
        if (roomDataObject.north.wallType != Wall.WALL_TYPE.NONE){
            if (roomDataObject.north.wallType != Wall.WALL_TYPE.WALL_GLASS){
                buildRoomBox(meshBuilderWalls,builtShape,0,0,-ROOM_RADIUS_CENTER,ROOM_DIAMETER_INNER,ROOM_DIAMETER_INNER,ROOM_WALL_THICKNESS);
            }
        }
        if (roomDataObject.south.wallType != Wall.WALL_TYPE.NONE){
            if (roomDataObject.south.wallType != Wall.WALL_TYPE.WALL_GLASS){
                buildRoomBox(meshBuilderWalls,builtShape,0,0,ROOM_RADIUS_CENTER,ROOM_DIAMETER_INNER,ROOM_DIAMETER_INNER,ROOM_WALL_THICKNESS);
            }
        }

        Node node2b = modelBuilder.node();
        node2b.id = "walls";
        MeshPartBuilder meshBuilderFloors = modelBuilder.part("floors", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, matFloors);
        //Build floor and ceiling
        if (roomDataObject.ceiling.wallType != Wall.WALL_TYPE.NONE){
            if (roomDataObject.ceiling.wallType != Wall.WALL_TYPE.WALL_GLASS){
                buildRoomBox(meshBuilderFloors,builtShape,0,ROOM_RADIUS_CENTER,0,ROOM_DIAMETER_INNER,ROOM_WALL_THICKNESS,ROOM_DIAMETER_INNER);
            }
        }
        if (roomDataObject.floor.wallType != Wall.WALL_TYPE.NONE){
            if (roomDataObject.floor.wallType != Wall.WALL_TYPE.WALL_GLASS) {
                buildRoomBox(meshBuilderFloors,builtShape, 0, -ROOM_RADIUS_CENTER, 0, ROOM_DIAMETER_INNER, ROOM_WALL_THICKNESS, ROOM_DIAMETER_INNER);
            }
        }

        //Build glass walls / transparent objects
        Node node3 = modelBuilder.node();
        node3.id = "wallsGlass";
        MeshPartBuilder meshBuilderGlassWalls = modelBuilder.part("wallsGlass", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, matGlassWalls);

        //Put down the glass walls
        if (roomDataObject.ceiling.wallType != Wall.WALL_TYPE.NONE){
            if (roomDataObject.ceiling.wallType == Wall.WALL_TYPE.WALL_GLASS){
                buildRoomBox(meshBuilderGlassWalls,builtShape,0,ROOM_RADIUS_CENTER,0,ROOM_DIAMETER_INNER,ROOM_WALL_THICKNESS,ROOM_DIAMETER_INNER);
            }
        }
        if (roomDataObject.floor.wallType != Wall.WALL_TYPE.NONE){
            if (roomDataObject.floor.wallType == Wall.WALL_TYPE.WALL_GLASS) {
                buildRoomBox(meshBuilderGlassWalls,builtShape, 0, -ROOM_RADIUS_CENTER, 0, ROOM_DIAMETER_INNER, ROOM_WALL_THICKNESS, ROOM_DIAMETER_INNER);
            }
        }
        if (roomDataObject.east.wallType != Wall.WALL_TYPE.NONE){
            if (roomDataObject.east.wallType == Wall.WALL_TYPE.WALL_GLASS){
                buildRoomBox(meshBuilderGlassWalls,builtShape,ROOM_RADIUS_CENTER,0,0,ROOM_WALL_THICKNESS,ROOM_DIAMETER_INNER,ROOM_DIAMETER_INNER);
            }
        }
        if (roomDataObject.west.wallType != Wall.WALL_TYPE.NONE){
            if (roomDataObject.west.wallType == Wall.WALL_TYPE.WALL_GLASS){
                buildRoomBox(meshBuilderGlassWalls,builtShape,-ROOM_RADIUS_CENTER,0,0,ROOM_WALL_THICKNESS,ROOM_DIAMETER_INNER,ROOM_DIAMETER_INNER);
            }
        }
        if (roomDataObject.north.wallType != Wall.WALL_TYPE.NONE){
            if (roomDataObject.north.wallType == Wall.WALL_TYPE.WALL_GLASS){
                buildRoomBox(meshBuilderGlassWalls,builtShape,0,0,-ROOM_RADIUS_CENTER,ROOM_DIAMETER_INNER,ROOM_DIAMETER_INNER,ROOM_WALL_THICKNESS);
            }
        }
        if (roomDataObject.south.wallType != Wall.WALL_TYPE.NONE){
            if (roomDataObject.south.wallType == Wall.WALL_TYPE.WALL_GLASS){
                buildRoomBox(meshBuilderGlassWalls,builtShape,0,0,ROOM_RADIUS_CENTER,ROOM_DIAMETER_INNER,ROOM_DIAMETER_INNER,ROOM_WALL_THICKNESS);
            }
        }

        //Build doors
        Node node4 = modelBuilder.node();
        node4.id = "doors";
        MeshPartBuilder meshBuilderDoors = modelBuilder.part("doors", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, matDoors);

        if (roomDataObject.east.wallType == Wall.WALL_TYPE.DOOR){
            if (roomDataObject.east.wallType != Wall.WALL_TYPE.WALL_GLASS){
                buildRoomBox(meshBuilderWalls,builtShape,ROOM_RADIUS_CENTER,0,0-ROOM_DIAMETER_INNER*0.15f,ROOM_WALL_THICKNESS*4.0f,ROOM_DIAMETER_INNER*0.75f,ROOM_RADIUS);
            }
        }
        if (roomDataObject.west.wallType == Wall.WALL_TYPE.DOOR){
            if (roomDataObject.west.wallType != Wall.WALL_TYPE.WALL_GLASS){
                buildRoomBox(meshBuilderWalls,builtShape,-ROOM_RADIUS_CENTER,0,0-ROOM_DIAMETER_INNER*0.15f,ROOM_WALL_THICKNESS*4.0f,ROOM_DIAMETER_INNER*0.75f,ROOM_RADIUS);
            }
        }
        if (roomDataObject.north.wallType == Wall.WALL_TYPE.DOOR){
            if (roomDataObject.north.wallType != Wall.WALL_TYPE.WALL_GLASS){
                buildRoomBox(meshBuilderWalls,builtShape,0,0-ROOM_DIAMETER_INNER*0.15f,-ROOM_RADIUS_CENTER,ROOM_RADIUS,ROOM_DIAMETER_INNER*0.75f,ROOM_WALL_THICKNESS*4.0f);
            }
        }
        if (roomDataObject.south.wallType == Wall.WALL_TYPE.DOOR){
            if (roomDataObject.south.wallType != Wall.WALL_TYPE.WALL_GLASS){
                buildRoomBox(meshBuilderWalls,builtShape,0,0-ROOM_DIAMETER_INNER*0.15f,ROOM_RADIUS_CENTER,ROOM_RADIUS,ROOM_DIAMETER_INNER*0.75f,ROOM_WALL_THICKNESS*4.0f);
            }
        }

        builtModel = modelBuilder.end();

        builtShape.createAabbTreeFromChildren();
    }

    public RoomObject(Room roomDataObject) {
        super();

        this.roomDataObject = roomDataObject;

        buildRoomModel(roomDataObject);
        setModel(new ModelInstance(builtModel));

        //Create physics object
        roomPhysicsObject = new KinematicPhysicsEntity(this,builtShape);

        //Update position
        update();
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {
        //Update the world translation and physics position of this object based on the roomDataObject
        Matrix4 tempTransform = roomDataObject.getWorldTransform();
        Vector3 pos = new Vector3();
        tempTransform.getTranslation(pos);
        Quaternion rotation = new Quaternion();
        tempTransform.getRotation(rotation);

        Matrix4 scaledTransform = new Matrix4();
        scaledTransform.translate(pos.x * ROOM_DIAMETER, pos.y * ROOM_DIAMETER, pos.z * ROOM_DIAMETER);
        scaledTransform.rotate(rotation);
        setTransform(scaledTransform);

        //Position physics body and visual body
        setTransform(scaledTransform);
        roomPhysicsObject.updateFromTransform(scaledTransform);

        super.update();
    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////