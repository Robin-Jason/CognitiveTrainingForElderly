<template>
  <div>
    <div id="cargando">
      <img class="rotate" src="../../../assets/GameImg/MemoryGame/portal.png" alt="" />
      <p>Loading...</p>
    </div>
    <div id="game"></div>
    <div class="info-game">
      <div id="movimientos">翻牌数: 0</div>
      <div id="tiempo">00:00</div>
      <button class="noSelect" @click="juegoNuevo">
        新游戏
      </button>
      <button class="noSelect" @click="volver">
        返回
      </button>
    </div>
  </div>
</template>

<script>
import swal from 'sweetalert';
export default {
  name: 'MemoryGame',
  data() {
    return {
      juegoInstance: null,
    };
  },
  methods: {
    iniciarJuego() {
      if (this.juegoInstance) {
        this.juegoInstance.iniciarJuego();
      }
    },
    juegoNuevo() {
      if (this.juegoInstance) {
        this.juegoInstance.juegoNuevo();
      }
    },
    volver() {
      this.$router.push({ name: 'GameSelection' }).then(() => {
        window.location.reload();
      });
    },
  },
  mounted() {
    class juego {
      constructor() {
        this.NumerosAPI = new Array();
        this.tarjetas = new Array();
        this.NivelActual = 0;
        this.CuadrosNivel = [10];
        this.SeleccionadaUNO = null;
        this.SeleccionadaDOS = null;
        this.NTarjetasSeleccionadas = 0;
        this.ContadorVictoria = 0;
        this.movimientos = 0;
        this.containerCargando = document.getElementById('cargando');
        this.containerMovimientos = document.getElementById('movimientos');
        this.container = document.getElementById('game');
        this.time = true;
        this.dataAPI = {
          loading: true,
          error: null,
          data: {
            info: {},
            results: [],
          },
        };
        // 计时器
        this.primermovimiento = false;
        this.hora = 0;
        this.minutos = 0;
        this.segundos = 0;
        this.decimales = 0;
        this.tiempo = '';
        this.stop = true;
      }

      async fetchCharacters() {
        this.dataAPI = { loading: true, error: null };

        try {
          const response = await fetch(`https://rickandmortyapi.com/api/character/`);
          const data = await response.json();

          this.dataAPI = {
            loading: false,
            data: {
              info: data.info,
              results: data.results,
            },
          };
        } catch (error) {
          this.dataAPI = { loading: false, error: error };
        }
      }

      async iniciarJuego() {
        this.NivelActual = 0;
        this.elegirtarjeta = this.elegirtarjeta.bind(this);
        await this.fetchCharacters();

        for (let i = 0; i < this.dataAPI.data.info.count; i++) {
          this.NumerosAPI.push(i + 1);
        }

        this.NumerosAPI = this.NumerosAPI.sort(() => Math.random() - 0.5);
        this.NumerosAPI.length = 10;

        const LengthStatic = this.NumerosAPI.length;

        for (let i = 0; i < LengthStatic; i++) {
          this.NumerosAPI.push(this.NumerosAPI[i]);
        }

        this.tarjetas.length = this.NumerosAPI.length;

        this.NumerosAPI = this.NumerosAPI.sort(() => Math.random() - 0.5);
        for (let i = 0; i < this.tarjetas.length; i++) {
          this.PersonajeTemporal = {};

          try {
            const response = await fetch(
                `https://rickandmortyapi.com/api/character/${this.NumerosAPI[i]}`
            );
            const data = await response.json();

            this.PersonajeTemporal = data;
          } catch (error) {
            this.PersonajeTemporal = { error: error };
          }

          this.tarjetas[i] = document.createElement('div');
          this.tarjetas[i].classList.add('tarjeta');
          this.tarjetas[i].innerText = this.NumerosAPI[i];
          this.tarjetas[i].setAttribute('data-position', i);
          this.tarjetas[i].addEventListener('click', this.elegirtarjeta);
          this.tarjetas[i].innerHTML = `
            <div class="front vueltaFront" data-position="${i}"></div>
            <div class="back vueltaBack" data-position="${i}" style="background-image: url(${this.PersonajeTemporal.image});"></div>
          `;
          this.container.appendChild(this.tarjetas[i]);
        }
        this.containerCargando.style.display = 'none';
        this.container.style.display = 'flex';
      }

      agregarEventos(n) {
        this.tarjetas[n].addEventListener('click', this.elegirtarjeta);
      }

      eliminarEventos(n) {
        this.tarjetas[n].removeEventListener('click', this.elegirtarjeta);
      }

      elegirtarjeta(e) {
        if (this.time === true) {
          switch (this.NTarjetasSeleccionadas) {
            case 0:
              if (!this.primermovimiento) {
                this.IniciarCronometro();
              }
              this.primermovimiento = true;
              this.SeleccionadaUNO = e.target.dataset.position;
              this.tarjetas[this.SeleccionadaUNO].classList.add('rotar');
              this.eliminarEventos(this.SeleccionadaUNO);
              this.NTarjetasSeleccionadas++;
              this.movimientos++;
              this.containerMovimientos.innerText = `翻牌数: ${this.movimientos}`;
              break;
            case 1:
              this.movimientos++;
              this.containerMovimientos.innerText = `翻牌数: ${this.movimientos}`;
              this.SeleccionadaDOS = e.target.dataset.position;
              this.tarjetas[this.SeleccionadaDOS].classList.add('rotar');
              if (this.NumerosAPI[this.SeleccionadaUNO] === this.NumerosAPI[this.SeleccionadaDOS]) {
                console.log('correcto');
                this.eliminarEventos(this.SeleccionadaDOS);
                this.ContadorVictoria++;
                if (this.ContadorVictoria === this.CuadrosNivel[this.NivelActual]) {
                  setTimeout(() => {
                    this.victoria();
                  }, 1000);
                }
              } else {
                console.log('incorrecto');
                this.time = false;
                setTimeout(() => {
                  this.tarjetas[this.SeleccionadaUNO].classList.remove('rotar');
                  this.tarjetas[this.SeleccionadaDOS].classList.remove('rotar');
                  this.time = true;
                }, 1000);
                this.agregarEventos(this.SeleccionadaUNO);
              }
              this.NTarjetasSeleccionadas = 0;
              break;
          }
        }
      }

      victoria() {
        this.PausarTiempo();
        // 使用 swal 或其他提示方式展示胜利信息
        swal(
            '太棒了!',
            `翻牌数: ${this.movimientos} \n\n 用时: ${this.tiempo}`,
            'success'
        ).then(() => {
          console.log('hola');
        });
      }

      juegoNuevo() {
        location.reload();
      }

      getRndInteger(min, max) {
        return Math.floor(Math.random() * (max - min + 1)) + min;
      }

      // Cronometro
      IniciarCronometro() {
        if (this.stop === true) {
          this.stop = false;
          this.cronometro();
        }
      }

      cronometro() {
        if (this.stop === false) {
          this.decimales++;
          if (this.decimales > 9) {
            this.decimales = 0;
            this.segundos++;
          }
          if (this.segundos > 59) {
            this.segundos = 0;
            this.minutos++;
          }
          if (this.minutos > 59) {
            this.minutos = 0;
            this.hora++;
          }
          this.verCronometro();
          setTimeout(() => this.cronometro(), 100);
        }
      }

      verCronometro() {
        this.tiempo = `${this.hora < 10 ? '0' : ''}${this.hora}:${this.minutos < 10 ? '0' : ''}${this.minutos}:${this.segundos < 10 ? '0' : ''}${this.segundos}`;
        document.getElementById('tiempo').innerHTML = this.tiempo;
      }

      PausarTiempo() {
        this.stop = true;
      }

      ReiniciarTiempo() {
        this.stop = true;
        this.hora = this.minutos = this.segundos = this.decimales = 0;
        this.tiempo = '';
        this.verCronometro();
      }
    }

    this.juegoInstance = new juego();
    this.juegoInstance.iniciarJuego();
  },
};
</script>

<style>
* {
  padding: 0;
  margin: 0;
  font-family: 'Bangers', cursive;
}

body {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  width: 100%;
  background-color: black;
  background-image: url(../../../assets/GameImg/MemoryGame/fondo.jpg);
  background-size: 105%;
  background-position-y: 90%;
  background-repeat: no-repeat;
  background-size: cover;
}

.hiden {
  display: none;
}

#cargando {
  display: flex;
  align-items: center;
  justify-content: space-around;
  width: 150px;
  height: 50px;
  background-color: rgba(0, 0, 0, 0.5);
  border-radius: 5px;
  color: white;
  position: relative;
}

#cargando img {
  height: 70%;
}

#game {
  display: none;
  height: 500px;
  width: 500px;
  flex-wrap: wrap;
  perspective: 600px;
  justify-content: space-around;
}

.info-game {
  width: 500px;
  height: 100px;
  background-color: rgba(255, 255, 255, 0.5);
  position: absolute;
  bottom: 20px;
  border-radius: 5px;
  display: flex;
  justify-content: space-around;
  align-items: center;
}

.info-game button {
  height: 70%;
  width: 20%;
  background-color: #ffff00;
  outline: none;
  border: none;
  border-radius: 5px;
  transition: 0.2s;
  cursor: pointer;
}

.info-game button:hover {
  font-size: 2em;
  background-color: #ffff34;
}

.tarjeta {
  display: flex;
  height: 90px;
  width: 90px;
  justify-content: center;
  align-items: center;
  margin: 5px 5px;
  border-radius: 25px;
  position: relative;
  cursor: pointer;
  transform-style: preserve-3d;
  transform-origin: center right;
  transition: transform 1s;
  -webkit-touch-callout: none;
  -webkit-user-select: none;
  -khtml-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
  -webkit-tap-highlight-color: transparent;
}

.rotar {
  transform: translateX(-100%) rotateY(-180deg);
}

.front,
.back {
  position: absolute;
  width: 100%;
  height: 100%;
  color: white;
  text-align: center;
  font-weight: bold;
  font-size: 35px;
  backface-visibility: hidden;
}

.vueltaFront {
  background-image: url(../../../assets/GameImg/MemoryGame/background.jpg);
  background-size: cover;
  border-radius: 15px;
}

.vueltaBack {
  transform: rotateY(180deg);
  background-size: cover;
  border-radius: 15px;
}

.video-container {
  width: 100%;
  height: 100vh;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
}

.video-container video {
  position: absolute;
  top: 0;
  left: 0;
  object-fit: cover;
  width: 100%;
  height: 100%;
  pointer-events: none;
  opacity: 50%;
}

.video-container .contenido {
  position: relative;
  z-index: 1;
  margin: 0 auto;
  text-align: center;
  width: 50vw;
}

.contenido img {
  width: 100%;
  margin-bottom: 100px;
}

.noSelect {
  -webkit-touch-callout: none;
  -webkit-user-select: none;
  -khtml-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
  -webkit-tap-highlight-color: transparent;
}

.button {
  cursor: pointer;
  overflow: hidden;
  padding: 20px;
  border: solid 2px #ffff00;
  background-color: #ffff00;
  border-radius: 35px;
  position: relative;
  top: 80%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 200px;
  text-align: center;
  font-family: sans-serif;
  font-weight: 600;
  letter-spacing: 5px;
  transition: all 0.8s cubic-bezier(0.4, 0, 0.2, 1);
  -webkit-text-stroke: 0px white;
}

.contenido a {
  color: rgb(56, 56, 56);
  text-decoration: none;
}

.button::after {
  transition: all 0.6s cubic-bezier(0.4, 0, 0.2, 1);
  content: '';
  width: 10px;
  height: 2px;
  background: white;
  position: absolute;
  top: 0px;
  right: 0px;
  box-shadow: 0px 5px 0px white, 0px 10px 0px white, 0px 15px 0px white,
  0px 20px 0px white, 0px 25px 0px white, 0px 30px 0px white,
  0px 35px 0px white, 0px 40px 0px white, 0px 45px 0px white,
  0px 50px 0px white, 0px 55px 0px white;
}
.button:hover::after {
  width: 160px;
  z-index: -1;
  -webkit-text-stroke: 4px white;
  box-shadow: -10px 5px 0px white, -20px 10px 0px white, -30px 15px 0px white,
  -40px 20px 0px white, -50px 25px 0px white, -60px 30px 0px white,
  -70px 35px 0px white, -80px 40px 0px white, -90px 45px 0px white,
  -100px 50px 0px white, -110px 55px 0px white;
}
.button:hover {
  color: #fff;
}

@media screen and (max-width: 1024px) {
}
@media screen and (max-width: 768px) {
  .video-container .contenido {
    width: 80vw;
  }
  .button {
    width: 150px;
  }
  #game {
    height: 400px;
    width: 300px;
  }
  .info-game {
    width: 300px;
  }

  .tarjeta {
    height: 60px;
    width: 60px;
  }
  .info-game button {
    width: 35%;
  }
}
@media screen and (max-width: 480px) {
}
@media screen and (max-width: 320px) {
  #game {
    height: 380px;
    width: 280px;
  }
  .info-game {
    width: 280px;
  }

  .tarjeta {
    height: 50px;
    width: 50px;
  }
}

.rotate {
  animation: rotation 4s infinite linear;
}

@keyframes rotation {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(359deg);
  }
}
</style>