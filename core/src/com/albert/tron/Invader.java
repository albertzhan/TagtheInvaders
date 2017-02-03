package com.albert.tron;

import java.util.Random;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;

public class Invader {
	public ModelInstance instance;//carries position
	//public Camera cam;
	public btCollisionObject collisionObject;
	boolean good;
	public Invader(ModelInstance instance,btCollisionObject collisionObject){
		this.instance = instance;
		this.collisionObject = collisionObject;
		randomizeLocation();
	}
	private void randomizeLocation(){
		Random rand = new Random();
		float x = (float) (14*rand.nextDouble() -8);//random float from -8 -> 8
		float y = (float) (14*rand.nextDouble()-10);//makes sure it is within the region
		float z = (float) (14*rand.nextDouble()-8);
		instance.transform.translate(x, y, z);
		//instance.transform.rotate(new Vector3(2*rand.nextFloat()-1,2*rand.nextFloat()-1,2*rand.nextFloat()-1), new Vector3(rand.nextFloat(),rand.nextFloat(),rand.nextFloat()));
		collisionObject.setWorldTransform(instance.transform);	
	}
}
