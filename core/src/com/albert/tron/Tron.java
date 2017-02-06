package com.albert.tron;

/**Playing with 3d
 * Using the tutorial stuffs from Xoppa
 * models and everything are on his github
 * There is a bug where sometimes the model and camera do not cooperate after finishing
 * I have tried orienting the model in every manner, then quitting, however can not accurately
 * replicate this bug. To fix, simply rotate the ship and then click q. This should resolve the issue
 */

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.CubemapAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.CollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionAlgorithm;
import com.badlogic.gdx.physics.bullet.collision.btCollisionAlgorithmConstructionInfo;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDispatcherInfo;
import com.badlogic.gdx.physics.bullet.collision.btManifoldResult;
import com.badlogic.gdx.physics.bullet.collision.btSphereBoxCollisionAlgorithm;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
/**
 * The goal of the game is to collect the invaders
 * some invaders are good, some are bad
 * You must stay within the certain boundaries
 * Hitting the bad invaders or the boundaries will lose you the game
 * Once all the good invaders are collected, winner is decided by the most collected
 * There will always be 20, starting out as all good
 * For each good one taken, another is generated bad
**/
public class Tron extends ApplicationAdapter implements InputProcessor{
	Vector3 moveTest2;

	PerspectiveCamera cam; 
	PerspectiveCamera cam2;
	PerspectiveCamera cam3;
	PerspectiveCamera cam4;
	PerspectiveCamera cam21;
	PerspectiveCamera cam22;
	Ship blue;
	Ship red;
	boolean collision;
	Cubemap spaceCube;

	Model gridlayer;
	ModelInstance gridlayerInstance;

	Model cube;
	Model model;
	Model packages;
	Model model2;
	Model spacemodel;
	ModelInstance instance;
	ModelInstance packageInstance;
	Array<ModelInstance> cubesface = new Array<ModelInstance>();//(of cubes)
	Array<btCollisionObject> cubesfaceObject = new Array<btCollisionObject>();
	ModelBatch modelBatch;
	AssetManager assets;
	ModelInstance cubeInstance;
	ModelInstance player1Instance;
	ModelInstance player2Instance;
	ModelInstance space;
    Array<ModelInstance> grids = new Array<ModelInstance>();
    Environment environment;
    boolean moving;
    boolean toggleGrid = false;
    
    Invader tmpinvader;
    Array<Invader> invaders = new Array<Invader>();
    btCollisionObject invaderObject;
    btCollisionObject packageObject;
    
    btCollisionConfiguration collisionConfig;
    btDispatcher dispatcher;
    btCollisionShape cubeShape;
    btCollisionObject cubeObject;
    btCollisionShape shipShape;
    btCollisionObject shipObject;
    btCollisionObject shipObject2;
    
    ShapeRenderer shapeRenderer;
    BitmapFont font;
    String mode;
	SpriteBatch batch;
	Texture img;
	Texture gameMenuGF;
	Texture coolRect;
	Texture telescope;
	Texture menuBGP;
	Texture menuBGI;
	Texture menuBGC;
	Texture controls;
	Texture instructions;
	Texture bluewins;
	Texture redwins;
	Texture invadersTagged;
	Texture spacePic;
	
	@Override
    public void create () {
		mode = "Menu";
		
		img = new Texture(Gdx.files.internal("badlogic.jpg"));
		//gameMenuGF = new Texture(Gdx.files.internal("menuBG.jpg"));
		gameMenuGF = new Texture(Gdx.files.internal("data/menuBG1.png"));
		menuBGP = new Texture(Gdx.files.internal("data/menuBGP.png"));
		menuBGI = new Texture(Gdx.files.internal("data/menuBGI.png"));
		menuBGC = new Texture(Gdx.files.internal("data/menuBGC.png"));
		controls = new Texture(Gdx.files.internal("data/Controls1.png"));
		bluewins = new Texture(Gdx.files.internal("data/bluewins.png"));
		redwins = new Texture(Gdx.files.internal("data/redwins.png"));
		coolRect = new Texture(Gdx.files.internal("data/coolRect.png"));
		telescope = new Texture(Gdx.files.internal("data/telescope.png"));
		invadersTagged = new Texture(Gdx.files.internal("data/InvadersTagged.png"));
		spacePic = new Texture(Gdx.files.internal("space.jpg"));
		batch = new SpriteBatch();
		font = new BitmapFont();
		shapeRenderer = new ShapeRenderer();
		
		Bullet.init();//initializing all bullet for collision
        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        //spaceCube = new Cubemap(Gdx.files.internal("space.jpg"),Gdx.files.internal("space.jpg"),Gdx.files.internal("space.jpg"),Gdx.files.internal("space.jpg"),Gdx.files.internal("space.jpg"),Gdx.files.internal("space.jpg"));
        collision = false;
        //setting up the environment
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 3f, 1f, 1f, 1f));
        environment.add(new DirectionalLight().set(0.2f, 0.2f, 0.2f, -1f, -0.8f, -0.2f));
        environment.set(new CubemapAttribute(CubemapAttribute.EnvironmentMap, spaceCube));
        //new modelbatch to do stuff in 3d
        /**modelBatch = new ModelBatch(new DefaultShaderProvider(
        	    Gdx.files.internal("Shaders/vertex.glsl"),
        	    Gdx.files.internal("Shaders/fragment.glsl")));**/
        modelBatch = new ModelBatch();

        //creating the grid layers
        ModelBuilder modelBuilder = new ModelBuilder();
        gridlayer = modelBuilder.createLineGrid(10,10,2f,2f,
        		new Material(ColorAttribute.createDiffuse(Color.GREEN), ColorAttribute.createSpecular(1, 1, 1, 1), FloatAttribute.createShininess(8f)), 
        		Usage.Position | Usage.Normal);
        for (float i = -10; i < 10; ++i){
            gridlayerInstance = new ModelInstance(gridlayer);
            gridlayerInstance.transform.translate(0,i,0);
            grids.add(gridlayerInstance);
        }
        
		ModelLoader loader = new ObjLoader();
        model = loader.loadModel(Gdx.files.internal("ship2/ship.obj"));
        model2 = loader.loadModel(Gdx.files.internal("ship/ship.obj"));

        player1Instance = new ModelInstance(model);
        player2Instance = new ModelInstance(model2);
        spacemodel = loader.loadModel(Gdx.files.internal("spacesphere.obj"));//the space that surrounds everything
        space = new ModelInstance(spacemodel);
        shipShape = new btSphereShape(0.5f);
        shipObject = new btCollisionObject();
        shipObject2 = new btCollisionObject();
        blue = new Ship(player1Instance,5,0,5,cam,cam21,shipObject);
        red = new Ship(player2Instance,3,0,1,cam2,cam22,shipObject2);
        blue.collisionObject.setCollisionShape(shipShape);
        blue.collisionObject.setWorldTransform(blue.instance.transform);        
        red.collisionObject.setCollisionShape(shipShape);
        red.collisionObject.setWorldTransform(red.instance.transform);
        cubeShape = new btBoxShape(new Vector3(10f,10f,10f));
        
        packages = loader.loadModel(Gdx.files.internal("invader.obj"));

        font.getData().setScale(20);

        //setting up the models
        modelSetup();
    }

	@Override
	public void render () {//update state of game -called every couple of milliseconds
		Gdx.gl.glClearColor(135/255f, 206/255f, 235/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);//clearing the screen
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

        checkKeyPressed();//checks keys
        handleCollisions();//tagging the invaders
        handle3D();//everything with 3d in it (draws some 2d as well)
        handle2D();//the 2d overlay (maybe also calls 3d stuffs)
		//System.out.printf("%d %d\n", getMouse()[0],getMouse()[1]);
		checkGameEnd();
    }
	
	@Override
	public void dispose () {//Disposing stuff for memory control
        //cubeObject.dispose();
        //cubeShape.dispose();
		//System.out.printf("Blue: %d    Red: %d", blue.kills, red.kills);
        shipObject.dispose();
        shipShape.dispose();

        dispatcher.dispose();
        collisionConfig.dispose();
		//spaceCube.dispose();
		modelBatch.dispose();
		model.dispose();
		model2.dispose();
		batch.dispose();
		img.dispose();
		gameMenuGF.dispose();
	}
	public void handle3D(){
		if (mode.equals("Menu") || mode.equals("Controls") || mode.equals("Instructions")){
			renderSpaceCam();
		}
		if(mode.equals("Play")){
			cameraRender();
			advance(0.1f);
		}
		if(mode.equals("Over")){
			cameraRender();//they no longer advance
		}
	}
	public void handle2D(){
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.begin();
		if(mode.equals("Menu")){
			batch.draw(gameMenuGF, 0, 0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
			//play button
			if(mouseInRectangle(888,600,1100,680)){
				batch.draw(menuBGP, 0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
				if(Gdx.input.isTouched()){
					mode = "Play";
					modelSetup();
				}
			}
			//Controls
			if(mouseInRectangle(820,345,1170,440)){
				batch.draw(menuBGC,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
				if(Gdx.input.isTouched()){
					mode = "Controls";
				}
			}
			//Instructions
			if(mouseInRectangle(740,160,1250,250)){
				batch.draw(menuBGI,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
				if(Gdx.input.isTouched()){
					mode = "Instructions";
				}
			}
		}if(mode.equals("Controls")){
			batch.draw(controls,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
			if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)){
				mode = "Menu";
			}
		}if(mode.equals("Instructions")){
			if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)){
				mode = "Menu";
			}
		}if(mode.equals("Over")){
			if(blue.kills > 19){
				batch.draw(bluewins,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
			}if(red.kills > 19){
				batch.draw(redwins,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
			}
		}
		batch.end();
	}
	public void handleCollisions(){
		handleCollisionsShip(red);
		handleCollisionsShip(blue);

	}
	public void handleCollisionsShip(Ship killer){
		for (int i = 0; i < invaders.size; ++i){
			if (checkCollision(invaders.get(i).collisionObject,killer.collisionObject)){
				invaders.removeIndex(i);
				killer.kills++;
			}
		}
	}
	public void checkKeyPressed(){
		if (Gdx.input.isKeyJustPressed(Keys.SPACE) && mode.equals("Play")){
			moving = true;
		}
		if (Gdx.input.isKeyJustPressed(Keys.ENTER)){
			if (mode.equals("Over")){
				mode = "Menu";
				modelSetup();//resets things to escape over mode (or else kills > 19 and mode = over)
			}
		}
		if(Gdx.input.isKeyJustPressed(Keys.Q)){
			if(mode.equals("Play")){
				mode = "Menu";
				modelSetup();
			}
		}
		if(Gdx.input.isKeyJustPressed(Keys.G)){
			if(mode.equals("Play") || mode.equals("Over")){
				toggleGrid = (toggleGrid == false);//if false, it becomes true, if true it becomes false
			}
		}
        checkBluePressed();
        checkRedPressed();
	}
	public void modelSetup(){
		moving = false;
		blue.kills = 0;
		red.kills = 0;
		blue.instance.transform.setToTranslation(new Vector3(9,0,9));
		blue.instance.transform.setToRotation(new Vector3(0,1,0),0);
		red.instance.transform.setToTranslation(new Vector3(-9,0,-9));
		red.instance.transform.setToRotation(new Vector3(0,1,0),0);
		blue.position = new Vector3(9,0,9);
		red.position = new Vector3(-9,0,-9);
		blue.resetOrientation();
		red.resetOrientation();
		cameraSetup();
		blue.truerotate(2);//make the ship face the other way
		blue.truerotate(2);
		invaders = new Array<Invader>();
        for (int i = 0; i < 100; ++i){
            packageInstance = new ModelInstance(packages);
            packageObject = new btCollisionObject();
            packageInstance.transform.translate(new Vector3 (0,4,2));
            packageObject.setCollisionShape(shipShape);//just the basic sphere
            packageObject.setWorldTransform(packageInstance.transform);
       
            tmpinvader = new Invader(packageInstance, packageObject);
            invaders.add(tmpinvader);
        }
	}
	public void cameraSetup(){
        blue.cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		blue.cam.position.set(blue.position.add(0,1.7f,-2.8f));
        blue.cam.lookAt(blue.position);
        blue.cam.near = 0.01f;
        blue.cam.far = 300f;
        blue.cam.update();
        
        blue.cam2 = new PerspectiveCamera(67,Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/4);
        blue.cam2.position.set(blue.position.add(-1.5f,1,-0.8f));
        blue.cam2.lookAt(blue.position);
        blue.cam2.near = 1f;
        blue.cam2.far = 300f;
        blue.cam2.update();
        
        red.cam = new PerspectiveCamera(67,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        red.cam.position.set(red.position.add(0,1,-2));
        red.cam.lookAt(red.position);
        red.cam.near = 0.01f;
        red.cam.far = 300f;
        red.cam.update();
        
        red.cam2 = new PerspectiveCamera(67,Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/4);
        red.cam2.position.set(red.position.add(1.5f,1,-0.8f));
        red.cam2.lookAt(red.position);
        red.cam2.near = 1f;
        red.cam2.far = 300f;
        red.cam2.update();
        
        cam3 = new PerspectiveCamera(67,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        cam3.position.set(0,0,0);
        cam3.lookAt(0,1,0);
        cam3.near = 1f;
        cam3.far = 300f;
        cam3.update();
        red.advance(0.001f);
        blue.advance(0.001f);
        
        cam4 = new PerspectiveCamera(67,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        cam4.position.set(0,18,0);
        cam4.lookAt(0,0,0);
        cam4.near = 1f;
        cam4.far = 300f;
        cam4.update();
	}
	public void renderSpaceCam(){//space view that i'll use for the menu
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		modelBatch.begin(cam3);
		modelBatch.render(space);
		cam3.rotateAround(new Vector3(1,0,0), new Vector3(1,1,1),0.1f);
		cam3.update();
		modelBatch.end();
	}
	public void cameraRender(){
        /**------------Rendering with cameras------------**/
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		//Left Half bLue
	    Gdx.gl.glViewport( 0,0,Gdx.graphics.getWidth()/2,3*Gdx.graphics.getHeight()/4 );
        modelBatch.begin(blue.cam);
        //blue.cam.lookAt(blue.position);
        blue.cam.update();
        modelBatch.render(red.instance,environment);
        modelBatch.render(blue.instance,environment);
        if(toggleGrid)modelBatch.render(grids,environment);
        //modelBatch.render(cubeInstance,environment);
        //modelBatch.render(cubesface.get(0),environment);
        modelBatch.render(packageInstance,environment);
        modelBatch.render(space,environment);
        for (Invader i: invaders){
        	modelBatch.render(i.instance);
        }
        modelBatch.end();
        //blue top
        Gdx.gl.glViewport(0,3*Gdx.graphics.getHeight()/4,Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/4);        
        
        batch.begin();
        batch.draw(coolRect,0,0,2*Gdx.graphics.getWidth()/3,Gdx.graphics.getHeight());
        batch.draw(invadersTagged, 80, -120,Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight());
        font.draw(batch, Integer.toString(blue.kills), 300f, 500f);
        //batch.draw(telescope,950,200,2*Gdx.graphics.getWidth()/3-1050,Gdx.graphics.getHeight()-40);
        batch.end();
        
        modelBatch.begin(blue.cam2);
        blue.cam2.update();
        modelBatch.render(blue.instance,environment);
        modelBatch.end();
        
        //Right Half Red
        Gdx.gl.glViewport( Gdx.graphics.getWidth()/2,0,Gdx.graphics.getWidth()/2,3*Gdx.graphics.getHeight()/4 );
        modelBatch.begin(red.cam);
        red.cam.update();
        modelBatch.render(red.instance,environment);
        modelBatch.render(blue.instance,environment);
        for (Invader i: invaders){
        	modelBatch.render(i.instance);
        }
        if(toggleGrid)modelBatch.render(grids,environment);
        modelBatch.render(space);
        modelBatch.end();
        
        //red top
        Gdx.gl.glViewport(2*Gdx.graphics.getWidth()/3, 3*Gdx.graphics.getHeight()/4, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/4);
        //this is the corner
        batch.begin();
        //batch.draw(spacePic, 0, 0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch.draw(coolRect,0,0,2*Gdx.graphics.getWidth()/3,Gdx.graphics.getHeight());
        batch.draw(invadersTagged, 80, -120,Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight());
        font.draw(batch, Integer.toString(red.kills), 300f, 500f);
        //batch.draw(telescope,950,200,2*Gdx.graphics.getWidth()/3-1050,Gdx.graphics.getHeight()-40);
        batch.end();
        
        modelBatch.begin(red.cam2);
        red.cam2.update();
        modelBatch.render(red.instance,environment);
        modelBatch.end();
        //Middle
        
        Gdx.gl.glViewport(Gdx.graphics.getWidth()/3, 3*Gdx.graphics.getHeight()/4, Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight()/4);
        modelBatch.begin(cam4);
        cam4.lookAt(0, 0, 0);
        if(moving)cam4.rotateAround(new Vector3(0,0,0), new Vector3(1,0,0), 0.1f);
        cam4.update();
        modelBatch.render(red.instance,environment);
        modelBatch.render(blue.instance,environment);
        if(toggleGrid)modelBatch.render(grids,environment);
        modelBatch.render(space,environment);
        for (Invader i: invaders){
        	modelBatch.render(i.instance);
        }
        modelBatch.end();	

	}
	public void advance(float speed){
		if (moving){
	        red.advance(speed);
	        blue.advance(speed);
		}

	}
    boolean checkCollision(btCollisionObject obj0, btCollisionObject obj1) {
    	//ripped off of Xoppa's tutorial
        CollisionObjectWrapper co0 = new CollisionObjectWrapper(obj0);
        CollisionObjectWrapper co1 = new CollisionObjectWrapper(obj1);

        btCollisionAlgorithm algorithm = dispatcher.findAlgorithm(co0.wrapper, co1.wrapper);

        btDispatcherInfo info = new btDispatcherInfo();
        btManifoldResult result = new btManifoldResult(co0.wrapper, co1.wrapper);

        algorithm.processCollision(co0.wrapper, co1.wrapper, info, result);

        boolean r = result.getPersistentManifold().getNumContacts() > 0;

        dispatcher.freeCollisionAlgorithm(algorithm.getCPointer());
        result.dispose();
        info.dispose();
        co1.dispose();
        co0.dispose();

        return r;
    }
	public void checkRedPressed(){
        if (Gdx.input.isKeyJustPressed(Keys.UP)){
        	red.rotate(0);
        	//red.instance.transform.rotate(new Vector3(-1,0,0),90);
        }
        if (Gdx.input.isKeyJustPressed(Keys.DOWN)) {
        	red.rotate(1);
        	//red.instance.transform.rotate(new Vector3(-1,0,0),-90);
        }
        if (Gdx.input.isKeyJustPressed(Keys.RIGHT)){
        	red.rotate(2);
        	//red.instance.transform.rotate(new Vector3(0,1,0), -90);//winding function angles, sticking the vector and rotating relative to it
        }
        if (Gdx.input.isKeyJustPressed(Keys.LEFT)){
        	red.rotate(3);
        	//red.instance.transform.rotate(new Vector3(0,1,0), 90);
        }
	}
	public void checkGameEnd(){
		if(blue.kills > 19 || red.kills > 19){
			mode = "Over";
			moving = false;
		}
	}
	public void checkBluePressed(){
		//System.out.println(blue.direction + " " + blue.upDirection + " " + blue.rightDirection);
        if (Gdx.input.isKeyJustPressed(Keys.W)){
        	blue.rotate(0);
        	//blue.rotate(new Vector3(0,1,0)); //blue.instance.transform.rotate(new Vector3(0,0,1),90)
        }
        if (Gdx.input.isKeyJustPressed(Keys.S)) {
        	blue.rotate(1);
        }
        if (Gdx.input.isKeyJustPressed(Keys.D)){
        	blue.rotate(2);
        	//blue.y += 0.1;
        	
        }
        if (Gdx.input.isKeyJustPressed(Keys.A)){
        	blue.rotate(3);
        	//blue.y += 0.1;	
        }
	}
	public int[] getMouse(){
		int [] toreturn = new int[2];
		toreturn[0] = Gdx.input.getX();
		toreturn[1] = Gdx.graphics.getHeight()-Gdx.input.getY();
		return toreturn;
	}
	public boolean mouseInRectangle(int x1, int y1, int x2, int y2){
		if (getMouse()[0] > x1 && getMouse()[0] < x2 && getMouse()[1] > y1 && getMouse()[1] < y2){
			return true;
		}
		return false;
	}
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}