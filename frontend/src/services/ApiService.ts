import axios, { AxiosResponse } from "axios";

/**
 * Classe que fornece métodos para interagir com a API.
 */
class ApiService {
  /**
   * URL base da API (pode ser modificada).
   * @private
   * @static
   * @type {string}
   */
  private static apiBaseUrl = "http://localhost:8080";

  /**
   * Opções padrão para as solicitações HTTP.
   * @private
   * @static
   * @type {Object}
   */
  private static defaultOptions = {
    headers: {
      "Content-Type": "application/json", // Define o tipo de conteúdo como JSON.
      "Access-Control-Allow-Origin": "http://localhost:3000", // Especifica a origem permitida.
    },
  };

  /**
   * Envia uma mensagem para ser introduzida a fila SQS.
   * @static
   * @param {string} message - O conteúdo da mensagem.
   * @param {string} type - O tipo de mensagem.
   * @returns {Promise<AxiosResponse | any>} - A resposta da solicitação ou erro.
   */
  static async sendMessage(
    message: string,
    type: string
  ): Promise<AxiosResponse | any> {
    try {
      const response = await axios.post(`${this.apiBaseUrl}/feedback`, {
        message,
        type: type.toUpperCase(),
      });
      console.log("response:", response); // Lida com os dados da resposta aqui.
      return response;
    } catch (error) {
      console.error("Houve um problema na solicitação Axios:", error);
      return error;
    }
  }

  /**
   * Obtém tamanhos das filas SQS.
   * @static
   * @returns {Promise<AxiosResponse | any>} - A resposta da solicitação ou erro.
   */
  static async getQueueSizes(): Promise<AxiosResponse | any> {
    try {
      const response = await axios.get(`${this.apiBaseUrl}/feedback/size`);
      console.log("response:", response.data);
      return response.data;
    } catch (error) {
      console.error("Houve um problema na solicitação Axios:", error);
      return error;
    }
  }

  /**
   * Endpoint de teste. Obtém uma saudação da API.
   * @static
   * @returns {Promise<AxiosResponse | any>} - A resposta da solicitação ou erro.
   */
  static async getHello(): Promise<AxiosResponse | any> {
    try {
      const response = await axios.get(
        `${this.apiBaseUrl}/hello`,
        this.defaultOptions
      );
      console.log("response:", response.data);
      return response;
    } catch (error) {
      console.error("Houve um problema na solicitação Axios:", error);
      return error;
    }
  }

  /**
   * Define a URL base da API.
   * @static
   * @param {string} url - A URL base da API.
   * @returns {string} - A URL base da API atualizada.
   */
  static setBaseUrl(url: string): string {
    return (this.apiBaseUrl = url);
  }

  /**
   * Obtém a URL base da API.
   * @static
   * @returns {string} - A URL base da API.
   */
  static getBaseUrl(): string {
    return this.apiBaseUrl;
  }
}

export default ApiService;
