package racoonman.racoongame.client.renderer.light;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import racoonman.racoongame.client.renderer.player.PlayerRenderTickContext;
import racoonman.racoongame.client.renderer.shader.LevelShader;
import racoonman.racoongame.core.tick.TickingObject;

public class LightRenderer implements TickingObject<PlayerRenderTickContext> {
	private LightInfo sceneLight;
    private float lightAngle = -90;
    
	public LightRenderer() {
		this.sceneLight = new LightInfo();
		this.sceneLight.setAmbientLight(new Vector3f(0.3f, 0.3f, 0.3f));

        float lightIntensity = 0.03f;
        Vector3f lightPosition = new Vector3f(-1, 0, 0);
        this.sceneLight.setDirectionalLight(new DirectionalLight(new Vector3f(0.5F, 0.5F, 0.5F), lightPosition, lightIntensity));
	}
	
	@Override
	public void tick(PlayerRenderTickContext ctx) {
		LevelShader.INSTANCE.setUniform("ambientLight", this.sceneLight.getAmbientLight());
        LevelShader.INSTANCE.setUniform("specularPower", 1.0F);
        Matrix4f viewMatrix = ctx.getViewMatrix();

        // Get a copy of the directional light object and transform its position to view coordinates
        DirectionalLight currDirLight = new DirectionalLight(this.sceneLight.getDirectionalLight());
        Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
        dir.mul(viewMatrix);
        currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
        LevelShader.INSTANCE.setUniform("directionalLight", currDirLight);
        
        // Update directional light direction, intensity and colour
        DirectionalLight directionalLight = this.sceneLight.getDirectionalLight();
        this.lightAngle += 0.1f;
        if (this.lightAngle > 90) {
            directionalLight.setIntensity(0);
            if (this.lightAngle >= 360) {
            	this.lightAngle = -90;
            }
        } else if (this.lightAngle <= -80 || this.lightAngle >= 80) {
            float factor = 1 - (float) (Math.abs(this.lightAngle) - 80) / 10.0f;
            directionalLight.setIntensity(factor);
//            directionalLight.getColor().y = Math.max(factor, 0.9f);
//            directionalLight.getColor().z = Math.max(factor, 0.5f);
        } else {
            directionalLight.setIntensity(1);
        }
        double angRad = Math.toRadians(this.lightAngle);
        directionalLight.getDirection().x = (float) Math.sin(angRad);
        directionalLight.getDirection().y = (float) Math.cos(angRad);
	}
}
