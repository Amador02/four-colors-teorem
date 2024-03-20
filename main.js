import { Map, View } from 'ol';
import Feature from 'ol/Feature';
import Polygon from 'ol/geom/Polygon';
import TileLayer from 'ol/layer/Tile';
import VectorLayer from 'ol/layer/Vector';
import { useGeographic } from 'ol/proj';
import OSM from 'ol/source/OSM';
import Vector from 'ol/source/Vector';
import Fill from 'ol/style/Fill';
import Stroke from 'ol/style/Stroke';
import Style from 'ol/style/Style';

const departamentos = [];

useGeographic();

async function loadDepartamentos() {
  const response = await fetch('departamentos.geojson');
  const data = await response.json();

  data.features.forEach((feature) => {
    departamentos.push(
      new Feature({
        geometry: new Polygon(feature.geometry.coordinates),
        properties: feature.properties,
        vecinos: feature.vecinos,
      })
    );
  });
}

function departamentoStyle(color) {

  return new Style({
    stroke: new Stroke({
      color: '#000000',
      width: 1,
    }),

    fill: new Fill({
      color: 'black', // se supone que cambia con la funcion
    }),
  });
}


// no se usa aún
function esAdyacente(departamento1, departamento2) {
  return departamento2.get('vecinos').includes(departamento1.get('nombre'));
}

// logica para lo de los vecinos (guarda un array con cada id de cada depto y su color)
function colorearDepartamentos(departamentos) {
  const coloresAsignados = {};

  departamentos.forEach((departamento) => {
    const vecinos = departamento.get('vecinos');
    for (let color of coloresDisponibles) {

      if (!vecinos.some((vecino) => coloresAsignados.hasOwnProperty(vecino) && coloresAsignados[vecino] === color)) {
        coloresAsignados[departamento.get('id')] = color;
        console.log({ coloresAsignados });
        break;
      }
    }
  });
  return coloresAsignados;
}



loadDepartamentos().then(() => {

  const coloresAsignados = colorearDepartamentos(departamentos.slice());
  const departamentosLayer = new VectorLayer({
    source: new Vector({ features: departamentos }),
    style: (feature) => {
      // se usa el color que se calculo en el metodo de colorear
      const color = coloresAsignados[feature.getId()];
      return departamentoStyle(color);
    },

  });



  const map = new Map({
    target: 'map',
    layers: [
      // capa normal
      new TileLayer({
        source: new OSM(),
      }),
      // capa colores
      departamentosLayer],

    view: new View({

      // ubica en colombia (no sirve si no se pone useGeographic() al inicio)
      center: [-74.24, 4.59],
      zoom: 6,
    }),
  });

  departamentosLayer.setOpacity(0.5);

  map.render('map');

});


// usa 4 colores 
const coloresDisponibles = ["#ff0000", "#00ff00", "#0000ff", "#ffff00"];
