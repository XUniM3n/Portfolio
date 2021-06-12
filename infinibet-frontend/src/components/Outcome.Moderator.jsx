import React from "react";

export class ModeratorOutcome extends React.Component {
  render() {
    const {name, coefficient} = this.props;

    return (
        <>
          <span className="my-1">{name} | {coefficient}</span>
        </>
    );
  }
}