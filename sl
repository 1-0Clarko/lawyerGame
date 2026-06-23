6f9af1e refactor: extract QuestionButtonsUI from DialogueWithDefendantManager, fix trust gap and TRUSTING state
d3b6ae2 fix: canged internal monolog
98641b7 feat: add defendant trust states and game over animation
5aed628 feat: improve dialog and tweak answer/question display
688daec refactor: ConnectionRequirement to the dialog system
66fead5 refactor: canged InterogatoryDialog to have the possibly final dialog
4938f51 feat: added a RequireTrust flag to the DialogNode connections
c2ed0f3 refactor
a215630 Refactor: improved dialog system and improved Javadocs
6948247 feat: add clickable dialog choices in InterrogatoryScene
ce71f89 feat: add input system and refactor InterrogatoryScene
b478488 feat: add JSON-based dialog tree system and rename Fase to Scene
4ebf1b3 fix: animation queue now correctly triggers first animation callbacks
e00f9ff feat: Added Dialog System Refactor DialogNode interface Renamed DialogLoader to DialogTreeLoader Added a test dialog InterogatoryDialog added JSON_DialogTreeLoader to load a json diagle tree added SimpleDialogNode with rappresent a text screen, it is a single node inside a dialog tree
25e5fb4 feat: add dialog tree system
1697321 feat: add interrogatory scene with stop motion defendant animation
52cfb0a feat: clip geometry outside FRUSTUM and move FRUSTUM to constructor
e5465b6 Merge branch 'feature/first-game-fase'
e6aad7e feat: use text color in GDX_TextRenderer
eac849f feat: add removeObject and removeText to GraphicsManager
6d21e25 feat: add animation system and refactor StartScene
acfefab feat: support decimal charsPerFrame in SimpleMonologue
97a9040 fix: SimpleMonologue not showing first page
1ccc24c fix: render text on top of all objects by disabling depth test for text
4139611 feat: add game phases, monologue, opacity and noise shader
714a627 feat: add GameFase, Monologue and Sequence interfaces
c0a1168 Merge branch 'feature/text-object-in-GDX_GraphicsManager'
ead09a0 feat: add font support to text rendering
06c4ef6 feat: add text rendering support
9bbb040 refactor: centralize screen projection calculation in GDX_GraphicsManager
187a997 fix: update game logic at fixed 30 FPS regardless of window events
0fc775d fix: resend screen projection data to shader for every object on each render call
ef8a1f1 Merge branch 'feature/model-loader'
7c9219a feat: add PLY model loading and update demo
4b939a9 feat: add ModelLoader interface
5ede7e5 feat: apply object transform and depth sorting in GDX_MeshRenderer
2f9d91b Merge branch 'refactor/index-mesh-rendering'
1e48673 feat: implement index-based rendering in GDX_MeshRenderer
61f3a0f refactor: add validation default methods to RendableObject
2c34d57 feat: add triangle index support to RendableObject interface
69084f0 feat: remove u_max_depth uniform from ShadersSource
5bb450c fix: normalize color values and add example triangle
a69e334 fix: correct Vertex interface
f5c92e7 feat: add depth-based darkening to shaders
2629145 documentation: update code style guidelines in CONTRIBUTING.md
4d428c4 Merge branch 'feature/GenericObjects'
f5a142e feat: add GenericTextObject
a9a19dd feat: add Generic3DObject
c77a7bc fix: remove return type from setObjectVertices setter
7fa6131 documentation: add CONTRIBUTING.md with branch naming, commit and code style guidelines
1fe080a Merge branch 'refactor/graphics-manager'
0b302d7 Replaced GDX_TriangleTest
0033fe4 First commit: 3-module Gradle project setup (app, gameEngine, api)
