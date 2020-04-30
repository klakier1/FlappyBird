#version 330 core

layout(location = 0) out vec4 color;

in DATA {
	vec2 tc;
	vec4 pos;
} fs_in;

uniform sampler2D tex;
uniform vec2 birdPos;

void main() {
	color = (fs_in.pos + 10)/ 20; //postion to Color
	//color = vec4(1.0, 1.0, 1.0, 1.0) - vec4(fs_in.pos/10);
	color = texture(tex, fs_in.tc);

	color *= length(birdPos - fs_in.pos.xy) ;
}
