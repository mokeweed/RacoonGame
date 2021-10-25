package racoonman.racoongame.client.renderer.shader;

import racoonman.racoongame.client.renderer.core.Material;
import racoonman.racoongame.client.renderer.level.effects.Fog;
import racoonman.racoongame.client.renderer.light.DirectionalLight;
import racoonman.racoongame.client.renderer.light.PointLight;
import racoonman.racoongame.client.renderer.light.SpotLight;

//TODO reimplement shading
public class LevelShader extends ShaderProgram {
	public static final LevelShader INSTANCE = new LevelShader();
    private static final int MAX_POINT_LIGHTS = 5;
    private static final int MAX_SPOT_LIGHTS = 5;
	
	private LevelShader() {
		super("level.vs", "level.fs");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "uvs");
	}

	@Override
	protected void createUniforms() {
		super.createUniform("texture_sampler");
		//super.createUniform("normal_sampler");
		super.createUniform("projectionMatrix");
		super.createUniform("modelViewMatrix");
//		super.createUniform("specularPower");
//		super.createUniform("ambientLight");
		
//      this.createPointLightListUniform("pointLights", MAX_POINT_LIGHTS);
//      this.createSpotLightListUniform("spotLights", MAX_SPOT_LIGHTS);
	
//		super.createUniform("material.ambient");
//		super.createUniform("material.diffuse");
//		super.createUniform("material.specular");
		super.createUniform("material.hasTexture");
//		super.createUniform("material.reflectance");
//		super.createUniform("material.hasNormalMap");
//		
//		super.createUniform("directionalLight.color");
//		super.createUniform("directionalLight.direction");
//		super.createUniform("directionalLight.intensity");

//		super.createUniform("fog.enabled");
//		super.createUniform("fog.color");
//		super.createUniform("fog.density");
	}
	
	public void setFogUniforms(Fog fog) {
//		this.setUniform("fog.enabled", fog.isEnabled());
//		this.setUniform("fog.color", fog.getColor());
//		this.setUniform("fog.density", fog.getDensity());
	}
	
	public void createPointLightListUniform(String uniformName, int size) {
		for (int i = 0; i < size; i++) {
			this.createPointLightUniform(uniformName + "[" + i + "]");
		}	
	}	
	  
	public void createPointLightUniform(String uniformName) {
		super.createUniform(uniformName + ".color");
		super.createUniform(uniformName + ".position");
		super.createUniform(uniformName + ".intensity");
		super.createUniform(uniformName + ".att.constant");
		super.createUniform(uniformName + ".att.linear");
		super.createUniform(uniformName + ".att.exponent");
	}
	
	public void createSpotLightListUniform(String uniformName, int size) {
		for (int i = 0; i < size; i++) {
			this.createSpotLightUniform(uniformName + "[" + i + "]");
		}
    }

    public void createSpotLightUniform(String uniformName) {
    	this.createPointLightUniform(uniformName + ".pl");
        super.createUniform(uniformName + ".conedir");
        super.createUniform(uniformName + ".cutoff");
    }


	public void setMaterialUniform(Material material) {
//		super.setUniform("material.ambient", material.getAmbientColor());
//		super.setUniform("material.diffuse", material.getDiffuseColor());
//		super.setUniform("material.specular", material.getSpecularColor());
		super.setUniform("material.hasTexture", material.isTextured());
//		super.setUniform("material.reflectance", material.getReflectance());
//		super.setUniform("material.hasNormalMap", material.hasNormalMap());
	}
	
	public void setDirectionalLightUniform(DirectionalLight light) {
//		super.setUniform("directionalLight.color", light.getColor());
//		super.setUniform("directionalLight.direction", light.getDirection());
//		super.setUniform("directionalLight.intensity", light.getIntensity());
	}
	
	public void setUniform(String uniformName, PointLight pointLight) {
        setUniform(uniformName + ".color", pointLight.getColor());
        setUniform(uniformName + ".position", pointLight.getPosition());
        setUniform(uniformName + ".intensity", pointLight.getIntensity());
        PointLight.Attenuation att = pointLight.getAttenuation();
        setUniform(uniformName + ".att.constant", att.getConstant());
        setUniform(uniformName + ".att.linear", att.getLinear());
        setUniform(uniformName + ".att.exponent", att.getExponent());
    }

    public void setUniform(String uniformName, DirectionalLight dirLight) {
        setUniform(uniformName + ".color", dirLight.getColor());
        setUniform(uniformName + ".direction", dirLight.getDirection());
        setUniform(uniformName + ".intensity", dirLight.getIntensity());
    }
    
    public void setUniform(String uniformName, SpotLight spotLight) {
        setUniform(uniformName + ".pl", spotLight.getPointLight());
        setUniform(uniformName + ".conedir", spotLight.getConeDirection());
        setUniform(uniformName + ".cutoff", spotLight.getCutOff());
    }
    
    public void setUniform(String uniformName, SpotLight spotLight, int pos) {
        setUniform(uniformName + "[" + pos + "]", spotLight);
    }
    

    public void setUniform(String uniformName, PointLight pointLight, int pos) {
        setUniform(uniformName + "[" + pos + "]", pointLight);
    }
}
