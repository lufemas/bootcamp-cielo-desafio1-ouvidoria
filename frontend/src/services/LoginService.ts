/**
 * Classe responsável por fornecer serviços relacionados à autenticação e controle de login.
 */
class LoginService {
  /**
   * Lista de cargos de usuário válidos.
   * @type {string[]}
   */
  public static userRoles = ["administrator", "customer", "none"];

  /**
   * Usuário logado atualmente.
   * @type {string}
   * @private
   */
  private static loggedUser = "none";

  /**
   * Altera o usuário logado para o cargo de usuário especificado.
   * @static
   * @param {string} role - O cargo de usuário para o qual fazer login.
   * @returns {string} - O cargo de usuário atual após a alteração.
   */
  static loginAs = (role: string): string => {
    return (this.loggedUser = role);
  };

  /**
   * Obtém o cargo do usuário atualmente logado.
   * @static
   * @returns {string} - O cargo de usuário atual.
   */
  static getLogin = (): string => this.loggedUser;
}

export default LoginService;
