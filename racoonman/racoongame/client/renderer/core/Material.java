package racoonman.racoongame.client.renderer.core;

import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;

import racoonman.racoongame.client.renderer.shader.LevelShader;
import racoonman.racoongame.client.renderer.texture.Texture;

public class Material {
    private static final Vector4f DEFAULT_COLOUR = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
    private Vector4f ambientColor;
    private Vector4f diffuseColor;
    private Vector4f specularColor;
    private float reflectance;
    private Texture texture;
    private Texture normalMap;

    public Material() {
        this.ambientColor = DEFAULT_COLOUR;
        this.diffuseColor = DEFAULT_COLOUR;
        this.specularColor = DEFAULT_COLOUR;
        this.texture = null;
        this.reflectance = 0;
    }

    public Material(Vector4f color, float reflectance) {
        this(color, color, color, null, null, reflectance);
    }

    public Material(Texture texture) {
        this(DEFAULT_COLOUR, DEFAULT_COLOUR, DEFAULT_COLOUR, texture, null, 0);
    }

    public Material(Texture texture, float reflectance) {
        this(DEFAULT_COLOUR, DEFAULT_COLOUR, DEFAULT_COLOUR, texture, null, reflectance);
    }
    
    public Material(Texture texture, Texture normalMap) {
        this(DEFAULT_COLOUR, DEFAULT_COLOUR, DEFAULT_COLOUR, texture, normalMap, 0.0F);
    }
    
    public Material(Texture texture, Texture normalMap, float reflectance) {
        this(DEFAULT_COLOUR, DEFAULT_COLOUR, DEFAULT_COLOUR, texture, normalMap, reflectance);
    }

    public Material(Vector4f ambientColor, Vector4f diffuseColor, Vector4f specularColor, Texture texture, Texture normalMap, float reflectance) {
        this.ambientColor = ambientColor;
        this.diffuseColor = diffuseColor;
        this.specularColor = specularColor;
        this.texture = texture;
        this.normalMap = normalMap;
        this.reflectance = reflectance;
    }

    public Vector4f getAmbientColor() {
        return ambientColor;
    }

    public void setAmbientColor(Vector4f ambientColor) {
        this.ambientColor = ambientColor;
    }

    public Vector4f getDiffuseColor() {
        return diffuseColor;
    }

    public void setDiffuseColor(Vector4f diffuseColor) {
        this.diffuseColor = diffuseColor;
    }

    public Vector4f getSpecularColor() {
        return specularColor;
    }

    public void setSpecularColor(Vector4f specularColor) {
        this.specularColor = specularColor;
    }

    public float getReflectance() {
        return reflectance;
    }

    public void setReflectance(float reflectance) {
        this.reflectance = reflectance;
    }

    public boolean isTextured() {
        return this.texture != null;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
    
    public boolean hasNormalMap() {
    	return this.normalMap != null;
    }
    
    public Texture getNormalMap() {
    	return this.normalMap;
    }
    
    public void bind() {
		LevelShader.INSTANCE.setMaterialUniform(this);
		
    	if(this.isTextured())
    		this.texture.bind(GL20.GL_TEXTURE0);
    	if(this.hasNormalMap())
    		this.normalMap.bind(GL20.GL_TEXTURE1);
    }

    public void unBind() {
    	if(this.isTextured())
    		this.texture.unBind();
    	if(this.hasNormalMap())
    		this.normalMap.unBind();
    }
}