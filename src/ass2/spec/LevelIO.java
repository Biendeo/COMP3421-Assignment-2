package ass2.spec;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import ass2.game.LSystemTrees;
import ass2.math.Vector3;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * COMMENT: Comment LevelIO
 *
 * @author malcolmr
 */
public class LevelIO {

	/**
	 * Load a terrain object from a JSON file
	 *
	 * @param mapFile
	 * @return
	 * @throws FileNotFoundException
	 */
	public static Terrain load(File mapFile) throws FileNotFoundException {

		Reader in = new FileReader(mapFile);
		JSONTokener jtk = new JSONTokener(in);
		JSONObject jsonTerrain = new JSONObject(jtk);

		int width = jsonTerrain.getInt("width");
		int depth = jsonTerrain.getInt("depth");
		Terrain terrain = new Terrain(width, depth);

		int LSystemTreesGenerations = jsonTerrain.optInt("LSystemTreesGenerations", 4);
		terrain.setTreeGens(LSystemTreesGenerations);

		JSONArray jsonSun = jsonTerrain.getJSONArray("sunlight");
		float dx = (float)jsonSun.getDouble(0);
		float dy = (float)jsonSun.getDouble(1);
		float dz = (float)jsonSun.getDouble(2);
		terrain.setSunlightDir(dx, dy, dz);

		JSONArray jsonAltitude = jsonTerrain.getJSONArray("altitude");
		for (int i = 0; i < jsonAltitude.length(); i++) {
			int x = i % width;
			int z = i / width;

			double h = jsonAltitude.getDouble(i);
			terrain.setGridAltitude(x, z, h);
		}

		if (jsonTerrain.has("trees")) {
			JSONArray jsonTrees = jsonTerrain.getJSONArray("trees");
			for (int i = 0; i < jsonTrees.length(); i++) {
				JSONObject jsonTree = jsonTrees.getJSONObject(i);
				double x = jsonTree.getDouble("x");
				double z = jsonTree.getDouble("z");
				terrain.addTree(x, z);
			}
		}

		if (jsonTerrain.has("roads")) {
			JSONArray jsonRoads = jsonTerrain.getJSONArray("roads");
			for (int i = 0; i < jsonRoads.length(); i++) {
				JSONObject jsonRoad = jsonRoads.getJSONObject(i);
				double w = jsonRoad.getDouble("width");

				JSONArray jsonSpine = jsonRoad.getJSONArray("spine");
				double[] spine = new double[jsonSpine.length()];

				for (int j = 0; j < jsonSpine.length(); j++) {
					spine[j] = jsonSpine.getDouble(j);
				}
				terrain.addRoad(w, spine);
			}
		}

		if (jsonTerrain.has("portals")) {
			JSONArray jsonPortals = jsonTerrain.getJSONArray("portals");
			for (int i = 0; i < jsonPortals.length(); i++) {
				JSONObject jsonPortalPair = jsonPortals.getJSONObject(i);

				JSONObject jsonPortal1 = jsonPortalPair.getJSONObject("end1");
				double portal1X = jsonPortal1.getDouble("x");
				double portal1Z = jsonPortal1.getDouble("z");
				double portal1Angle = jsonPortal1.getDouble("angle");

				JSONObject jsonPortal2 = jsonPortalPair.getJSONObject("end2");
				double portal2X = jsonPortal2.getDouble("x");
				double portal2Z = jsonPortal2.getDouble("z");
				double portal2Angle = jsonPortal2.getDouble("angle");

				double portalWidth = jsonPortalPair.getDouble("width");
				double portalHeight = jsonPortalPair.getDouble("height");

				terrain.addPortalPair(portal1X, portal1Z, portal1Angle, portal2X, portal2Z, portal2Angle, portalWidth, portalHeight);
			}
		}

		if (jsonTerrain.has("monsters")) {
			JSONArray jsonMonsters = jsonTerrain.getJSONArray("monsters");
			for (int i = 0; i < jsonMonsters.length(); i++) {
				JSONObject jsonMonster = jsonMonsters.getJSONObject(i);

				double speed = jsonMonster.getDouble("speed");
				JSONArray jsonMonsterPath = jsonMonster.getJSONArray("path");
				ArrayList<Vector3> path = new ArrayList<>();

				for (int j = 0; j < jsonMonsterPath.length(); j += 2) {
					path.add(new Vector3(jsonMonsterPath.getDouble(j), 0.0, jsonMonsterPath.getDouble(j + 1)));
				}

				terrain.addMonster(path, speed);
			}
		}
		return terrain;
	}

	/**
	 * Write Terrain to a JSON file
	 *
	 * @param file
	 * @throws IOException
	 */
	public static void save(Terrain terrain, File file) throws IOException {
		JSONObject json = new JSONObject();

		Dimension size = terrain.size();
		json.put("width", size.width);
		json.put("depth", size.height);

		JSONArray jsonSun = new JSONArray();
		float[] sunlight = new float[]{terrain.getSunlight().x, terrain.getSunlight().y, terrain.getSunlight().z};
		jsonSun.put(sunlight[0]);
		jsonSun.put(sunlight[1]);
		jsonSun.put(sunlight[2]);
		json.put("sunlight", jsonSun);

		JSONArray altitude = new JSONArray();
		for (int i = 0; i < size.width; i++) {
			for (int j = 0; j < size.height; j++) {
				altitude.put(terrain.getGridAltitude(i, j));
			}
		}
		json.put("altitude", altitude);

		JSONArray trees = new JSONArray();
		for (LSystemTrees t : terrain.trees()) {
			JSONObject j = new JSONObject();
			Vector3 position = t.transform.position;
			j.put("x", position.x);
			j.put("z", position.z);
			trees.put(j);
		}
		json.put("trees", trees);

		JSONArray roads = new JSONArray();
		for (Road r : terrain.roads()) {
			JSONObject j = new JSONObject();
			j.put("width", r.width());

			JSONArray spine = new JSONArray();
			int n = r.size();

			for (int i = 0; i <= n*3; i++) {
				double[] p = r.controlPoint(i);
				spine.put(p[0]);
				spine.put(p[1]);
			}
			j.put("spine", spine);
			roads.put(j);
		}
		json.put("roads", roads);

		FileWriter out = new FileWriter(file);
		json.write(out);
		out.close();

	}

	/**
	 * For testing.
	 *
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		Terrain terrain = LevelIO.load(new File(args[0]));
		LevelIO.save(terrain, new File(args[1]));
	}

}
