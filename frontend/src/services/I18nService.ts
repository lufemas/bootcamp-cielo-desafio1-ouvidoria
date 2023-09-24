/**
 * Classe responsável por fornecer serviços de internacionalização (i18n).
 */
class I18nService {
  /**
   * Dicionário de tokens para tradução para Português (Brasil).
   * @type {Record<string, string>}
   */
  public static tokens_pt_br: Record<string, string> = {
    suggestion: "Sugestão",
    suggestions: "Sugestões",
    criticism: "Crítica",
    criticisms: "Críticas",
    praise: "Elogio",
    praises: "Elogios",
    size: "Tamanho",

    none: "Nenhum",
    administrator: "Administrador",
    customer: "Cliente",

    homepage: "Início",
    loggeduser: "Usuário Logado",
    serveraddress: "Endereço do Servidor",

    message: "Mensagem",
    messageform: "Formulário de Mensagem",
    messagesent: "A Mensagem foi enviada com sucesso.",
    messagesenderror: "Ocorreu um erro ao enviar a mensagem",

    send: "Enviar",

    menusendmessage: "Enviar Mensagem",
    menulistmessages: "Listar Mensagens",
    menuconsumemessage: "Consumir Mensagens",
    selectOption: "Selecionar Opção",
    loggedout: "Sem Sessão",
  };

  /**
   * Traduz um token para o idioma configurado (Português - Brasil).
   * @static
   * @param {string} token - O token a ser traduzido.
   * @param {Object} options - Opções adicionais, como plural.
   * @param {boolean} options.plural - Indica se o token deve ser pluralizado.
   * @returns {string} - O token traduzido.
   */
  static translate = (token: string, options: any = {}): string => {
    let finalToken = token.toLowerCase();
    if (options.plural) finalToken += "s";
    return this.tokens_pt_br?.[finalToken] || finalToken;
  };
}

export default I18nService;
