import React from "react";

export class PlayerOutcome extends React.Component {
  render() {
    const {id, name, coefficient, onChangeFunc} = this.props;

    return (
        <div>
          <input type="radio" name="outcome" value={id} onChange={onChangeFunc}/>{name} | {coefficient}
        </div>
    );
  }
}