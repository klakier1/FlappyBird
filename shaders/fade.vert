#version 330 core

const vec4 vert[6] = vec4[6](vec4(-1.0f, -1.0f, 0.0f, 1.0f),
                             vec4( 1.0f, -1.0f, 0.0f, 1.0f),
                             vec4( 1.0f,  1.0f, 0.0f, 1.0f),
                             vec4( 1.0f,  1.0f, 0.0f, 1.0f),
                             vec4(-1.0f,  1.0f, 0.0f, 1.0f),
                             vec4(-1.0f, -1.0f, 0.0f, 1.0f));

void main() {
	gl_Position = vert[gl_VertexID];
}
