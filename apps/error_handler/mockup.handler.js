class mockUpHandler extends TypeError {
  constructor(message) {
    super(message);
    // Ensure the name of this error is the same as the class name
    this.name = this.constructor.name;

  }
}
module.exports=mockUpHandler
