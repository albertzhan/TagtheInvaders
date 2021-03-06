package com.albert.tron;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Array;
/**Each "Tron" is a ship
They will leave behind a trail
instance will have a BoundingBox
ship will die when the box hits either the border or the trail
**/
public class Ship {
	public ModelInstance instance;
	public Vector3 position;
	//private Vector3 currPosition;
	public Vector3 direction;
	public Vector3 upDirection;
	public Vector3 rightDirection;
	public Camera cam;
	public Camera cam2;
	public Camera fakecam;
	private Vector3 currDirection;//to replace and switch the directions
	public btCollisionObject collisionObject;
	public int kills = 0;
	public int turning = 0;
	public int turnangle = 5;
	public Ship (ModelInstance instance, float x, float y, float z, Camera cam,Camera cam2, btCollisionObject collisionObject){

		this.instance = instance;
		position = new Vector3(x,y,z);
		resetOrientation();
//		instance.transform.setToTranslation(position);
//		upDirection = new Vector3(0,1,0); //the ship's upside
//		rightDirection = new Vector3(-1,0,0);
//		direction = new Vector3(0,0,1);//points in positive z direction in the normal model when loaded in
		
		this.collisionObject = collisionObject;
		this.cam = cam;
		this.cam = cam2;
	}
	public btCollisionObject checkCollided(Array<btCollisionObject> boxes){
		return boxes.get(0);
	}
	public void resetOrientation(){
		turning = 0;
		turnangle = 5;
		instance.transform.setToTranslation(position);
		upDirection = new Vector3(0,1,0); //the ship's upside
		rightDirection = new Vector3(-1,0,0);
		direction = new Vector3(0,0,1);//points in positive z direction in the normal model when loaded in
	}
	public void advance(float speed){
		//walls are when coords hit 9.5 
		/**starts out going in positive z
		 * up makes it 0,1,0
		 * right is add x
		 * left is minus x
		**/
		/**calculate if it is on the border
		 * using bullet physics for this is overkill
		 * the absolute of the coords cannot go over 9.5
		 */
		if (turning > 0){
			turning --;
			if (turnangle == 0){
	        	instance.transform.rotate(new Vector3(-1,0,0),5);
	        	cam.rotateAround(position, rightDirection, 5);
			}
			if (turnangle == 1){
	        	instance.transform.rotate(new Vector3(-1,0,0),-5);//rotates the model
				cam.rotateAround(position, rightDirection, -5);//makes the camera go around it
			}
			if (turnangle == 2){
	        	instance.transform.rotate(new Vector3(0,1,0), -5);//winding function angles, sticking the vector and rotating relative to it
	        	cam.rotateAround(position, upDirection, -5);
			}
			if (turnangle == 3){
	        	instance.transform.rotate(new Vector3(0,1,0), 5);
	        	cam.rotateAround(position, upDirection, 5);
			}
			if (turning == 0){
				turnangle = 5;
			}
        	return;
		}else{
			instance.transform.translate(new Vector3 (0,0,speed));//always makes the instance go forward
			position = instance.transform.getTranslation(new Vector3());//takes in what position the instance is at
			if (Math.abs(position.x) >= 9.5 || Math.abs(position.y) >= 9.5 || Math.abs(position.z) >= 9.5){
				instance.transform.translate(new Vector3(0,0,-1*speed));//undoes the moving forward
				position = instance.transform.getTranslation(new Vector3());
			}else{
				cam.lookAt(position);
				cam2.lookAt(position);
				cam.translate(timesVector(direction,speed));
				cam2.translate(timesVector(direction,speed));
			}
		}


		//update the collision
		collisionObject.setWorldTransform(instance.transform);
	}
	public void rotate(int angle){
		/**I can only to 90 degree rotations because I'm a noob at math and can't figure out
		 * how to do the vector calculations
		 *  (for the camera, otherwise it's just change the 90's to whatever angle),
		 * so I use 3 vectors that go around instead
		 * up is 0, down is 1, right is 2, left is 3
		 * up: direction = updirection, updirection = -direction
		 * down: direction = -updirection, updirection = direction
		 * right: direcion = rightdirection, rightdirection = -direction
		 * left: direction = -rightdirection, rightdirection = direction
		**/
		if (turning > 0){
			return;//if it's turning then i don't allow it to overload
		}
		if (angle == 0){//up
//        	instance.transform.rotate(new Vector3(-1,0,0),90);
//        	cam.rotateAround(position, rightDirection, 90);
        	
			currDirection = direction;
			direction = upDirection;
			upDirection = negVector(currDirection);
			turning = 18;
			turnangle = 0;
		}
		if (angle == 1){//down
//        	instance.transform.rotate(new Vector3(-1,0,0),-90);//rotates the model
//			cam.rotateAround(position, rightDirection, -90);//makes the camera go around it
        	
			currDirection = direction;
			direction = negVector(upDirection);
			upDirection = currDirection;
			turning = 18;
			turnangle = 1;
		}
		if (angle == 2){//right
//        	instance.transform.rotate(new Vector3(0,1,0), -90);//winding function angles, sticking the vector and rotating relative to it
//        	cam.rotateAround(position, upDirection, -90);

			currDirection = direction;
			direction = rightDirection;
			rightDirection = negVector(currDirection);
			turning = 18;
			turnangle = 2;
		}
		if (angle == 3){//left
//        	instance.transform.rotate(new Vector3(0,1,0), 90);
//        	cam.rotateAround(position, upDirection, 90);

			currDirection = direction;
			direction = negVector(rightDirection);
			rightDirection = currDirection;
			turning = 18;
			turnangle = 3;
		}
	}
	public void truerotate(int angle){
		if (angle == 0){//up
			instance.transform.rotate(new Vector3(-1,0,0),90);
			cam.rotateAround(position, rightDirection, 90);

			currDirection = direction;
			direction = upDirection;
			upDirection = negVector(currDirection);

		}
		if (angle == 1){//down
			instance.transform.rotate(new Vector3(-1,0,0),-90);//rotates the model
			cam.rotateAround(position, rightDirection, -90);//makes the camera go around it

			currDirection = direction;
			direction = negVector(upDirection);
			upDirection = currDirection;

		}
		if (angle == 2){//right
			instance.transform.rotate(new Vector3(0,1,0), -90);//winding function angles, sticking the vector and rotating relative to it
			cam.rotateAround(position, upDirection, -90);

			currDirection = direction;
			direction = rightDirection;
			rightDirection = negVector(currDirection);

		}
		if (angle == 3){//left
			instance.transform.rotate(new Vector3(0,1,0), 90);
			cam.rotateAround(position, upDirection, 90);

			currDirection = direction;
			direction = negVector(rightDirection);
			rightDirection = currDirection;

		}
	}
	private Vector3 negVector(Vector3 toChange){
		/**takes in a vector and multiplies it by -1**/
		Vector3 toreturn =  new Vector3(-1*toChange.x,-1*toChange.y,-1*toChange.z);
		return toreturn;
	}
	private Vector3 timesVector(Vector3 toChange, float times){
		/**takes in a vector and multiplies it**/
		Vector3 toreturn =  new Vector3(times*toChange.x,times*toChange.y,times*toChange.z);
		return toreturn;
	}

}
