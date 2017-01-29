package com.albert.tron;


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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
//2 player 3d Tron
//splitscreen (like mariokart), using lighting from environment to make it easier to see
public class Tron extends ApplicationAdapter implements InputProcessor{
	public Vector3 moveTest2;
	
	public PerspectiveCamera cam; 
	public PerspectiveCamera cam2;
	public PerspectiveCamera cam3;
	public PerspectiveCamera cam21;
	public PerspectiveCamera cam22;
	public Ship blue;
	public Ship red;
	
	public FileHandle space;
	public Cubemap spaceCube;
	
	public Model gridlayer;
	public ModelInstance gridlayerInstance;
	
	public Model cube;
    public Model model;
    public Model model2;
    public ModelInstance instance;
    public Array<ModelInstance> cubesface = new Array<ModelInstance>();//(of cubes)
    public ModelBatch modelBatch;
    public AssetManager assets;
    public ModelInstance cubeInstance;
    public ModelInstance player1Instance;
    public ModelInstance player2Instance;
    public Array<ModelInstance> grids = new Array<ModelInstance>();
    public Environment environment;
    public boolean loading;
    public int x;
    
	SpriteBatch batch;
	Texture img;

	@Override
    public void create () {
		x = 0;
        Vector3 moveTest2 = new Vector3(0,0.1f,0);
	  
        space = Gdx.files.internal("badlogic.jpg");

        //spaceCube = new Cubemap(Gdx.files.internal("space.jpg"),Gdx.files.internal("space.jpg"),Gdx.files.internal("space.jpg"),Gdx.files.internal("space.jpg"),Gdx.files.internal("space.jpg"),Gdx.files.internal("space.jpg"));
        
        //setting up the environment
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 3f, 1f, 1f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        environment.set(new CubemapAttribute(CubemapAttribute.EnvironmentMap, spaceCube));
        //new modelbatch to do stuff
        /**modelBatch = new ModelBatch(new DefaultShaderProvider(
        	    Gdx.files.internal("Shaders/vertex.glsl"),
        	    Gdx.files.internal("Shaders/fragment.glsl")));**/
        modelBatch = new ModelBatch();

        //creating the grid layers
        ModelBuilder modelBuilder = new ModelBuilder();
        gridlayer = modelBuilder.createLineGrid(10,10,2f,2f,
        		new Material(ColorAttribute.createDiffuse(Color.GREEN), ColorAttribute.createSpecular(1, 1, 1, 1), FloatAttribute.createShininess(8f)), 
        		Usage.Position | Usage.Normal);
        for (float i = -10; i < 20; ++i){
            gridlayerInstance = new ModelInstance(gridlayer);
            gridlayerInstance.transform.translate(0,i,0);
            grids.add(gridlayerInstance);
        }
        //creating the sides of the cube (using more cubes)
        cube = modelBuilder.createBox(20f,20f, 20f,
        		new Material(TextureAttribute.createDiffuse(new Texture(space)))
        		/**new Material(ColorAttribute.createDiffuse(Color.BLUE))**/,
        		Usage.Position | Usage.Normal);
        cube.manageDisposable(new Texture(space));
        //sides of the cube
        cubeInstance = new ModelInstance(cube);
        cubeInstance.transform.translate(20, 0, 0);
        cubesface.add(cubeInstance);
        cubeInstance = new ModelInstance(cube);
        cubeInstance.transform.translate(-20, 0, 0);
        cubesface.add(cubeInstance);
        cubeInstance = new ModelInstance(cube);
        cubeInstance.transform.translate(0, 20, 0);
        cubesface.add(cubeInstance);
        cubeInstance = new ModelInstance(cube);
        cubeInstance.transform.translate(0, -20, 0);
        cubesface.add(cubeInstance);
        cubeInstance = new ModelInstance(cube);
        cubeInstance.transform.translate(0, 0, 20);
        cubesface.add(cubeInstance);
        cubeInstance = new ModelInstance(cube);
        cubeInstance.transform.translate(0, 0, -20);
        cubesface.add(cubeInstance);
        
        ModelLoader loader = new ObjLoader();
        model = loader.loadModel(Gdx.files.internal("ship2/ship.obj"));
        model2 = loader.loadModel(Gdx.files.internal("ship/ship.obj"));
        player1Instance = new ModelInstance(model);
        player2Instance = new ModelInstance(model2);
        
        blue = new Ship(player1Instance,5,0,5,cam,cam21);
        red = new Ship(player2Instance,3,0,1,cam2,cam22);
        
        //setting the cameras
        cameraSetup();
    }

	@Override
	public void render () {//update state of game -called every couple of milliseconds
		Gdx.gl.glClearColor(135/255f, 206/255f, 235/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);//clearing the screen
        
        checkKeyPressed();//checks rotations
        advance(0.1f);//advances the ships
        
        checkCollision();
        cameraRender();
       }
	
	@Override
	public void dispose () {
		
		//spaceCube.dispose();
		
		model.dispose();
		model2.dispose();
		//batch.dispose();
		//img.dispose();
	}
	public void checkKeyPressed(){
        checkBluePressed();
        checkRedPressed();
	}
	public void cameraSetup(){
        blue.cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		blue.cam.position.set(blue.position.add(0,1,-2));
        blue.cam.lookAt(blue.position);
        blue.cam.near = 1f;
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
        red.cam.near = 1f;
        red.cam.far = 300f;
        red.cam.update();
        
        red.cam2 = new PerspectiveCamera(67,Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/4);
        red.cam2.position.set(red.position.add(1.5f,1,-0.8f));
        red.cam2.lookAt(red.position);
        red.cam2.near = 1f;
        red.cam2.far = 300f;
        red.cam2.update();
        
        cam3 = new PerspectiveCamera(67,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        cam3.position.set(0,50f,0);
        cam3.lookAt(0,0,0);
        cam3.near = 1f;
        cam3.far = 300f;
        cam3.update();
	}
	public void cameraRender(){
        /**------------Rendering with cameras------------**/
		//Left Half bLue
	    Gdx.gl.glViewport( 0,0,Gdx.graphics.getWidth()/2,3*Gdx.graphics.getHeight()/4 );
        modelBatch.begin(blue.cam);
        //blue.cam.lookAt(blue.position);
        blue.cam.update();
        modelBatch.render(red.instance,environment);
        modelBatch.render(blue.instance,environment);
        modelBatch.render(grids,environment);
        modelBatch.render(cubeInstance,environment);
        modelBatch.end();
        //blue top
        Gdx.gl.glViewport(200,3*Gdx.graphics.getHeight()/4,Gdx.graphics.getWidth()/2-200,Gdx.graphics.getHeight()/4);
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
        //modelBatch.render(grids,environment);
        modelBatch.render(cubesface,environment);

        modelBatch.end();
        //red top
        Gdx.gl.glViewport(Gdx.graphics.getWidth()/2, 3*Gdx.graphics.getHeight()/4, Gdx.graphics.getWidth()/2-200, Gdx.graphics.getHeight()/4);
        modelBatch.begin(red.cam2);
        red.cam2.update();
        modelBatch.render(red.instance,environment);
        modelBatch.end();
        //Middle
        
        Gdx.gl.glViewport(Gdx.graphics.getWidth()/3, 3*Gdx.graphics.getHeight()/4, Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight()/4);
        modelBatch.begin(cam3);
        modelBatch.render(red.instance,environment);
        modelBatch.render(blue.instance,environment);
        modelBatch.render(gridlayerInstance,environment);
        modelBatch.end();	
	}
	public void advance(float speed){
		if (Gdx.input.isKeyPressed(Keys.SPACE)){
	        red.advance(speed);
	        blue.advance(speed);
		}

	}
	public void checkCollision(){
		int e;
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