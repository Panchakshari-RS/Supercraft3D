var renderer=null;
var renderer1 =null;
var obj = null;
var mat = null;
var scene = null;
var geo = null;
var obj1 = null;
var mat1 = null;
var scene1 = null;
var geo1 = null;

renderer = new THREE.WebGLRenderer();
renderer1 = new THREE.WebGLRenderer();

function disposethree()
{

	if(obj)
	{for(var j=0;j<obj.length;j++)
	{
	    obj[j].geometry.dispose();
	    obj[j].material.dispose();
	    scene.remove(obj[j]);
	     delete(obj[j]);
    }
}
	obj = null;
	mat = null;
	geo = null;
}
function disposethreeNew()
{
	if(obj1)
	{for(var j=0;j<obj1.length;j++)
	{
	    obj1[j].geometry.dispose();
	    obj1[j].material.dispose();
	    scene1.remove(obj1[j]);
	     delete(obj1[j]);
    }
}
	obj1 = null;
	mat1 = null;
	geo1= null;

}
	function initStl(w,h,view){
    "use strict";
console.log("here");

    renderer.setSize(w, h);
    renderer.domElement.id="stlviewcanvasfiller";
    //renderer.domElement.style.display = "none";

    view.appendChild(renderer.domElement);
console.log("here 1");

scene = new THREE.Scene();

var camera = new THREE.PerspectiveCamera(75, w / h, 0.5, 10000);
var bg_color=0x000000;
var controls = new THREE.OrbitControls(camera, view);
			renderer.setClearColor( bg_color, 1);
			camera.position.set(0,0,100);
			scene.add(camera);

			var ambientLight = new THREE.AmbientLight(0x202020);
			camera.add(ambientLight);

			var directionalLight = new THREE.DirectionalLight(0xffffff, 0.75);
			directionalLight.position.x = 1;
			directionalLight.position.y = 1;
			directionalLight.position.z = 2;
			directionalLight.position.normalize();
			camera.add(directionalLight);

			var pointLight = new THREE.PointLight(0xffffff, 0.3);
			pointLight.position.x = 0;
			pointLight.position.y = -25;
			pointLight.position.z = 10;
			camera.add(pointLight);

    mat = new THREE.MeshLambertMaterial({color:0x909090, overdraw: 1, wireframe: false, shading:THREE.FlatShading, vertexColors: THREE.FaceColors});

    var loop = function loop() {
        requestAnimationFrame(loop);
        controls.update();
        renderer.clear();
        renderer.render(scene, camera);
    };
    loop();

}

    function openFile(file) {
        var reader = new FileReader();
			console.log("name" + file.name);
        reader.addEventListener("load", function (ev) {

			setTimeout(function(){after_file_load("abc.stl", ev.target.result);}, 500);

        }, false);
        reader.readAsArrayBuffer(file);
    }
			function after_file_load(filename, s)
			{
				var vf_data;

				try
				{
					vf_data=parse_3d_file(filename, s);
				}
				catch(err)
				{
					vf_data="Error parsing the file";
				}

				if (typeof vf_data === 'string')
				{
					alert(vf_data);
					return;
				}

				if (obj!=null) {scene.remove(obj);obj=null};

				geo=new THREE.Geometry;
				geo.vertices=vf_data.vertices;
				geo.faces=vf_data.faces;
				geo.computeBoundingBox();

				geo.computeCentroids();
				geo.computeFaceNormals();
				geo.computeVertexNormals();
				THREE.GeometryUtils.center(geo);
				obj = new THREE.Mesh(geo, mat);
				scene.add(obj);
				renderer.domElement.style.display = "block";
				HoldOn.close();

		 }
	function initStlNew(w,h,view){
    "use strict";
console.log("here");

var view = document.getElementById("fullscreenSTL");

    renderer1.setSize(w, h);
    //renderer1.setViewport(100,100);
    //var view = document.getElementById("view");
console.log("here 1");
    renderer1.domElement.id="stlviewcanvasfillerfull";
    renderer1.domElement.style.display = "none";
    view.appendChild(renderer1.domElement);

scene1 = new THREE.Scene();

var camera = new THREE.PerspectiveCamera(75, w / h, 0.5, 10000);
var bg_color=0x000000;
var controls = new THREE.OrbitControls(camera, view);
			renderer1.setClearColor( bg_color, 1);
			camera.position.set(0,0,100);
			scene1.add(camera);

			var ambientLight = new THREE.AmbientLight(0x202020);
			camera.add(ambientLight);

			var directionalLight = new THREE.DirectionalLight(0xffffff, 0.75);
			directionalLight.position.x = 1;
			directionalLight.position.y = 1;
			directionalLight.position.z = 2;
			directionalLight.position.normalize();
			camera.add(directionalLight);

			var pointLight = new THREE.PointLight(0xffffff, 0.3);
			pointLight.position.x = 0;
			pointLight.position.y = -25;
			pointLight.position.z = 10;
			camera.add(pointLight);

    mat1 = new THREE.MeshLambertMaterial({color:0x909090, overdraw: 1, wireframe: false, shading:THREE.FlatShading, vertexColors: THREE.FaceColors});


    var loop = function loop() {
        requestAnimationFrame(loop);
        //obj1.rotation.z += 0.05;
        controls.update();
        renderer1.clear();
        renderer1.render(scene1, camera);
    };
    loop();

}
    function openFileNew(file) {
        var reader = new FileReader();
			console.log("name" + file.name);
        reader.addEventListener("load", function (ev) {

			setTimeout(function(){after_file_loadNew("abc.stl", ev.target.result);}, 500);

        }, false);
        reader.readAsArrayBuffer(file);
    }
			function after_file_loadNew(filename, s)
			{
				var vf_data;

				try
				{
					vf_data=parse_3d_file(filename, s);
				}
				catch(err)
				{
					vf_data="Error parsing the file";
				}

				if (typeof vf_data === 'string')
				{
					alert(vf_data);
					return;
				}

				if (obj1!=null) {scene1.remove(obj1);obj1=null};

				geo1=new THREE.Geometry;
				geo1.vertices=vf_data.vertices;
				geo1.faces=vf_data.faces;
				geo1.computeBoundingBox();

				geo1.computeCentroids();
				geo1.computeFaceNormals();
				geo1.computeVertexNormals();
				THREE.GeometryUtils.center(geo1);
				obj1 = new THREE.Mesh(geo1, mat1);
				scene1.add(obj1);
				renderer1.domElement.style.display = "block";
				HoldOn.close();
   //$("#fullscreenSTLMain").show();
 // flagFullScreenEntered = 0;
 // console.log("fulscreen");
   //toggleFullScreen(document.getElementById("fullscreenSTLMain"));

//				  toggleFullScreen(document.getElementById("fullscreenSTLMain"));


		 }