import React from "react";

function Logo(props) {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width={props.width}
      height={props.width}
      viewBox="0 0 20 20"
      fill={props.color}
    >
      <path d="M10 7.5a2.5 2.5 0 102.5 2.5A2.5 2.5 0 0010 7.5zm0 7a4.5 4.5 0 114.5-4.5 4.5 4.5 0 01-4.5 4.5zM10 3C3 3 0 10 0 10s3 7 10 7 10-7 10-7-3-7-10-7z"></path>
    </svg>
  );
}

export default Logo;
