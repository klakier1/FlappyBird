#version 330 core

layout(location = 0) in vec4 position;
layout(location = 1) in vec2 texCoord;

out DATA {
	vec2 tc;
	vec4 pos;
} vs_out;

uniform mat4 pr_matrix;
uniform mat4 mv_matrix;

void main() {

	vs_out.tc = texCoord;
	vs_out.pos = mv_matrix * position;
	gl_Position = pr_matrix * mv_matrix * position;
}
