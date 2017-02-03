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
	public Invader(ModelInstance instance,btCollisionObject collistionObject){
		this.instance = instance;
		this.collisionObject = collisionObject;
	}
//	private void randomizeLocation(){
//		Random rand = new Random();
//		float x = (float) (20*rand.nextDouble() -10);//random float from -10 -> 10
//		float y = (float) (20*rand.nextDouble()-10);
//		float z = (float) (20*rand.nextDouble()-10);
//		instance.transform.translate(x, y, z);
//		collisionObject.setWorldTransform(instance.transform);	
//	}
}
