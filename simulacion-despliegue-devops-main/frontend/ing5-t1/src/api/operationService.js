import axios from "axios";

// Por defecto "/api": en docker-compose nginx hace de proxy inverso hacia el
// backend, de modo que el navegador siempre habla con su propio origen y no
// hace falta que el puerto 8080 sea alcanzable desde fuera.
const baseURL = import.meta.env.VITE_API_BASE_URL || "/api";

const api = axios.create({
  baseURL,
  timeout: 10000,
  headers: { "Content-Type": "application/json" },
});

/**
 * Traduce un fallo de axios al mensaje que debe ver la persona usuaria.
 * El backend responde {"error": "..."} en los 4xx (p.ej. division por cero);
 * ese texto es mas util que un "Request failed with status code 400".
 */
function toMensajeDeError(error) {
  if (error.response) {
    return error.response.data?.error || `El servidor respondio ${error.response.status}.`;
  }
  if (error.code === "ECONNABORTED") {
    return "El servidor tardo demasiado en responder.";
  }
  return "No se pudo conectar con el servidor.";
}

export class OperationError extends Error {
  constructor(message, { status = null, cause = null } = {}) {
    super(message);
    this.name = "OperationError";
    this.status = status;
    this.cause = cause;
  }
}

/**
 * Las cuatro operaciones solo se diferencian en la ruta, asi que comparten
 * implementacion en vez de repetir cuatro veces el mismo try/catch.
 */
async function operar(ruta, a, b) {
  try {
    const { data } = await api.post(ruta, { a: Number(a), b: Number(b) });
    return data;
  } catch (error) {
    throw new OperationError(toMensajeDeError(error), {
      status: error.response?.status ?? null,
      cause: error,
    });
  }
}

export const sum = (a, b) => operar("/sumar", a, b);
export const sub = (a, b) => operar("/restar", a, b);
export const mult = (a, b) => operar("/multiplicar", a, b);
export const div = (a, b) => operar("/dividir", a, b);

export async function getHistorial() {
  try {
    const { data } = await api.get("/historial");
    return data;
  } catch (error) {
    throw new OperationError(toMensajeDeError(error), {
      status: error.response?.status ?? null,
      cause: error,
    });
  }
}
