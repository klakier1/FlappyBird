#version 330 core

layout(location = 0) out vec4 color;

in DATA {
	vec2 tc;
	vec4 pos;
}fs_in;

uniform sampler2D tex;
uniform int isTop;
uniform int hasColl;

void main() {
	//color = (fs_in.pos + 10)/ 20; //postion to Color
	//color = vec4(1.0, 1.0, 1.0, 1.0) - vec4(fs_in.pos/10);

	if (isTop == 1)
		color = texture(tex, vec2(fs_in.tc.x, 1 - fs_in.tc.y));
	else
		color = texture(tex, fs_in.tc);
	if (color.a != 1.0)
		discard;

	if (hasColl == 1)
		color.r = 0.7;

}
