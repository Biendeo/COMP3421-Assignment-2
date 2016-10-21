#version 120

// Based on the shader from here:
// http://www.fabiensanglard.net/bumpMapping/index.php

uniform sampler2D diffuseTexture;
uniform sampler2D normalTexture;
uniform sampler2D specularTexture;

// New bumpmapping
varying vec3 lightVec;
varying vec3 halfVec;
varying vec3 eyeVec;


void main() {

	// lookup normal from normal map, move from [0,1] to  [-1, 1] range, normalize
	vec3 normal = 2.0 * texture2D(normalTexture, gl_TexCoord[0].st).rgb - 1.0;
	normal = normalize (normal);

	vec4 specular = texture2D(specularTexture, gl_TexCoord[0].st).rgba;

	// compute diffuse lighting
	float lambertFactor = max (dot (lightVec, normal), 0.0);
	vec4 diffuseMaterial = vec4(0.0);
	vec4 diffuseLight = vec4(0.0);

	// compute specular lighting
	vec4 specularMaterial = vec4(1.0);
	vec4 specularLight = gl_LightSource[0].specular;
	float shininess = pow(max(dot(halfVec, normal), 0.0), 2.0);

	// compute ambient
	vec4 ambientLight = gl_LightSource[0].ambient;

	if (lambertFactor > 0.0) {
		diffuseMaterial = texture2D (diffuseTexture, gl_TexCoord[0].st);
		diffuseLight = gl_LightSource[0].diffuse;

		gl_FragColor = diffuseMaterial * diffuseLight * lambertFactor;
		gl_FragColor += specularMaterial * specularLight * shininess;
	}

	gl_FragColor += ambientLight;

}