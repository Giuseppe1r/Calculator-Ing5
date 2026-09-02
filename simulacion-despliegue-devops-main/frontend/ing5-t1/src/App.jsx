import { useCallback, useEffect, useState } from 'react'
import { sum, sub, mult, div, getHistorial } from './api/operationService'
import './App.css'

const OPERACIONES = [
  { id: 'suma', etiqueta: 'Sumar', simbolo: '+', fn: sum },
  { id: 'resta', etiqueta: 'Restar', simbolo: '\u2212', fn: sub },
  { id: 'multiplicacion', etiqueta: 'Multiplicar', simbolo: '\u00d7', fn: mult },
  { id: 'division', etiqueta: 'Dividir', simbolo: '\u00f7', fn: div },
]

// El backend devuelve doubles; se recortan los decimales sobrantes para no
// mostrar cosas como 0.30000000000000004.
function formatearNumero(valor) {
  if (typeof valor !== 'number' || !Number.isFinite(valor)) return String(valor)
  return Number(valor.toFixed(10)).toString()
}

function App() {
  const [num1, setNum1] = useState('')
  const [num2, setNum2] = useState('')
  const [operacion, setOperacion] = useState(null)
  const [error, setError] = useState(null)
  const [cargando, setCargando] = useState(false)
  const [historial, setHistorial] = useState([])

  const camposCompletos = num1.trim() !== '' && num2.trim() !== ''

  const refrescarHistorial = useCallback(async () => {
    try {
      setHistorial(await getHistorial())
    } catch {
      // El historial es informativo: si falla no se interrumpe la operacion.
    }
  }, [])

  useEffect(() => { refrescarHistorial() }, [refrescarHistorial])

  async function ejecutar({ fn }) {
    if (!camposCompletos || cargando) return

    setCargando(true)
    setError(null)
    try {
      // La respuesta es un OperacionResponseDto completo. Antes se pasaba tal
      // cual a setResult y React reventaba al intentar renderizar un objeto.
      const dto = await fn(num1, num2)
      setOperacion(dto)
      refrescarHistorial()
    } catch (e) {
      setOperacion(null)
      setError(e.message)
    } finally {
      setCargando(false)
    }
  }

  return (
    <main className="app">
      <h1> por que te mientes?</h1>
      <p className="subtitulo">Simulacion de despliegue DevOps &mdash; React + Spring Boot + MySQL</p>

      <section className="tarjeta">
        <div className="entradas">
          <label>
            <span>Numero 1</span>
            <input
              type="number"
              placeholder="0"
              value={num1}
              onChange={(e) => setNum1(e.target.value)}
            />
          </label>
          <label>
            <span>Numero 2</span>
            <input
              type="number"
              placeholder="0"
              value={num2}
              onChange={(e) => setNum2(e.target.value)}
            />
          </label>
        </div>

        <div className="botones">
          {OPERACIONES.map((op) => (
            <button
              key={op.id}
              onClick={() => ejecutar(op)}
              disabled={!camposCompletos || cargando}
            >
              <span aria-hidden="true">{op.simbolo}</span> {op.etiqueta}
            </button>
          ))}
        </div>

        <div className="salida" role="status" aria-live="polite">
          {cargando && <p className="cargando">Calculando&hellip;</p>}
          {!cargando && error && <p className="error">{error}</p>}
          {!cargando && !error && operacion && (
            <p className="resultado">
              <span className="operacion-texto">
                {formatearNumero(operacion.a)} {operacion.tipoOperacion} {formatearNumero(operacion.b)} =
              </span>
              <strong>{formatearNumero(operacion.resultado)}</strong>
            </p>
          )}
          {!cargando && !error && !operacion && (
            <p className="vacio">Introduce dos numeros y elige una operacion.</p>
          )}
        </div>
      </section>

      <section className="tarjeta">
        <h2>Ultimas operaciones</h2>
        {historial.length === 0 ? (
          <p className="vacio">Todavia no hay operaciones registradas.</p>
        ) : (
          <ul className="historial">
            {historial.map((item) => (
              <li key={item.id}>
                <span>
                  {formatearNumero(item.a)} {item.tipoOperacion} {formatearNumero(item.b)}
                </span>
                <strong>{formatearNumero(item.resultado)}</strong>
              </li>
            ))}
          </ul>
        )}
      </section>
    </main>
  )
}

export default App
